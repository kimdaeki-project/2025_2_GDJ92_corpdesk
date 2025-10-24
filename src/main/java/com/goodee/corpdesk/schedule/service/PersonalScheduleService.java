package com.goodee.corpdesk.schedule.service;

import com.goodee.corpdesk.schedule.dto.DocumentDTO;
import com.goodee.corpdesk.schedule.dto.GeocodeBodyDTO;
import com.goodee.corpdesk.schedule.dto.ReqPersonalScheduleDTO;
import com.goodee.corpdesk.schedule.dto.ResPersonalScheduleDTO;
import com.goodee.corpdesk.schedule.entity.PersonalSchedule;
import com.goodee.corpdesk.schedule.repository.PersonalScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class PersonalScheduleService {

    @Autowired
    private PersonalScheduleRepository personalScheduleRepository;

    // WebClient Bean 주입
    @Autowired
    private WebClient webClient;

    @Value("${api.kakao.restapi.key}")
    private String kakaoMapKey;

    public ResPersonalScheduleDTO createSchedule(String username, ReqPersonalScheduleDTO reqPersonalScheduleDTO) {

        PersonalSchedule newSchedule = reqPersonalScheduleDTO.toEntity();
        newSchedule.setUsername(username);
        newSchedule.setModifiedBy(username);

        return personalScheduleRepository.save(newSchedule).toResPersonalScheduleDTO();

    }

    // username, useYn, (year, month)로 일정 데이터들 조회
    public List<ResPersonalScheduleDTO> getSchedules(String username, ReqPersonalScheduleDTO reqPersonalScheduleDTO) {

        return personalScheduleRepository.findPersonalScheduleByUsernameAndYearMonth(true
                                                                                    , username
                                                                                    , reqPersonalScheduleDTO.getYear()
                                                                                    , reqPersonalScheduleDTO.getMonth());
        
    }

    public List<Integer> getYearRangeByUsername(String username) {

        // 유저의 가장 오래된 일정 year 반환
        Integer oldestYear = personalScheduleRepository.findOldestScheduleYearByUsername(true, username);
        // 유저의 가장 미래의 일정 year 반환
        Integer lastYear = personalScheduleRepository.findLastScheduleYearByUsername(true, username);

        // oldestYear ~ lastYear로 List 생성 (oldestYear가 없다면 오늘 날짜만 있는 List 리턴
        int currentYear = LocalDate.now().getYear();
        if(oldestYear == null) return List.of(currentYear);

        List<Integer> years = new ArrayList<>();
        for(int year = oldestYear; year <= lastYear; year++){
            years.add(year);
        }

        years.forEach(y -> log.warn("years - y: {}", y));

        return years;
        
    }

    public ResPersonalScheduleDTO getScheduleById(Long personalScheduleId) {

        return personalScheduleRepository.findPersonalScheduleByUseYnAndPersonalScheduleId(true, personalScheduleId).toResPersonalScheduleDTO();

    }

    public ResPersonalScheduleDTO updateSchedule(String modifiedBy, Long personalScheduleId, ReqPersonalScheduleDTO reqPersonalScheduleDTO) {

        // id로 조회
        PersonalSchedule oldSchedule = personalScheduleRepository.findPersonalScheduleByUseYnAndPersonalScheduleId(true, personalScheduleId);

        // save
        if(!modifiedBy.equals(oldSchedule.getUsername())) throw new SecurityException("본인 소유 일정만 수정할 수 있습니다.");

        oldSchedule.setModifiedBy(modifiedBy);
        oldSchedule.setScheduleName(reqPersonalScheduleDTO.getScheduleName());
        oldSchedule.setScheduleDateTime(reqPersonalScheduleDTO.getScheduleDateTime());
        oldSchedule.setContent(reqPersonalScheduleDTO.getContent());
        oldSchedule.setAddress(reqPersonalScheduleDTO.getAddress());

        return oldSchedule.toResPersonalScheduleDTO();

    }

    public ResPersonalScheduleDTO deleteSchedule(String modifiedBy, Long personalScheduleId) {

        // id로 조회
        PersonalSchedule oldSchedule = personalScheduleRepository.findPersonalScheduleByUseYnAndPersonalScheduleId(true, personalScheduleId);

        // delete
        if(!modifiedBy.equals(oldSchedule.getUsername())) throw new SecurityException("본인 소유 일정만 삭제할 수 있습니다.");

        oldSchedule.setModifiedBy(modifiedBy);
        oldSchedule.setUseYn(false);

        return oldSchedule.toResPersonalScheduleDTO();

    }

    public List<ResPersonalScheduleDTO> getSchedulesByDate(String username, LocalDateTime startOfDay, LocalDateTime endOfDay) {

        List<PersonalSchedule> schedules = personalScheduleRepository.findAllByUseYnAndUsernameAndScheduleDateTimeBetween(true,  username, startOfDay, endOfDay);

        if(schedules == null) return List.of();

        return schedules.stream().map(PersonalSchedule::toResPersonalScheduleDTO).toList();

    }

    // geocoding api 호출
    public List<ResPersonalScheduleDTO> bindGeocodesToSchedules(List<ResPersonalScheduleDTO> schedules) {

        for(ResPersonalScheduleDTO schedule : schedules) {

            if(schedule.getLatitude() != null && schedule.getLongitude() != null) {
                continue;
            }

            if(schedule.getAddress() == null || schedule.getAddress().trim().isEmpty()) {
                continue;
            }

            try {
                log.debug("Geocoding 시도 - 주소: {}", schedule.getAddress());

                // 주소를 URL 인코딩
                String encodedAddress = URLEncoder.encode(schedule.getAddress(), StandardCharsets.UTF_8);
                String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + encodedAddress;

                log.debug("요청 URL: {}", url);

                GeocodeBodyDTO geocodeBody = webClient.get()
                    .uri(url)
                    .header("Authorization", "KakaoAK " + kakaoMapKey)
                    .retrieve()
                    .bodyToMono(GeocodeBodyDTO.class)
                    .block();

                if(geocodeBody != null && geocodeBody.getDocuments() != null && !geocodeBody.getDocuments().isEmpty()) {
                    DocumentDTO geocodeInfo = geocodeBody.getDocuments().get(0);
                    schedule.setLatitude(Double.parseDouble(geocodeInfo.getY()));
                    schedule.setLongitude(Double.parseDouble(geocodeInfo.getX()));
                    log.debug("Geocoding 성공");
                }

                Thread.sleep(150);

            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("Geocoding 실패 - 주소: {}, 에러: {}", schedule.getAddress(), e.getMessage());
            }
        }

        return schedules;
    }

}
