package com.goodee.corpdesk.holiday.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.goodee.corpdesk.holiday.dto.HolidayDTO;
import com.goodee.corpdesk.holiday.dto.HolidayItemDTO;
import com.goodee.corpdesk.holiday.dto.HolidayResponseDTO;
import com.goodee.corpdesk.holiday.entity.Holiday;
import com.goodee.corpdesk.holiday.repository.HolidayRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class HolidayService {

    @Autowired
    private HolidayRepository holidayRepository;

    // WebClient Bean 주입
    @Autowired
    private WebClient webClient;

    @Value("${api.holiday.key}")
    private String key;

    private XmlMapper xmlMapper = new XmlMapper();

    public List<Holiday> fetchHoliday(Integer year) throws Exception {

        Mono<List<Holiday>> holidayListMono = webClient.get()
            .uri("http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo" +
                "?solYear=" + year + "&ServiceKey=" + key)
            .retrieve()
            .bodyToMono(String.class) // bodyToMono를 사용하여 전체 응답 객체(HolidayResponseDTO)를 받아옴
            .map(xmlString -> { // map 연산자를 사용해 HolidayItemDTO 리스트 추출
                log.warn("{}", xmlString);

                // String을 xmlMapper를 사용하여 HolidayResponseDTO로 변환
                HolidayResponseDTO responseDTO = null;
                try {
                    responseDTO = xmlMapper.readValue(xmlString, HolidayResponseDTO.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                return responseDTO.getBody().getItems().getItem();
            })
            .map(itemDTOs -> itemDTOs.stream() // 추출한 HolidayItemDTO 리스트를 Holiday 엔티티 리스트로 변환
                .map(this::convertToEntity)
                .collect(Collectors.toList()
            ));

        List<Holiday> holidays = holidayListMono.block(); // .block()을 사용하여 Mono가 완료될 때까지 기다리고 List<Holiday>를 반환받음
        log.warn("{}", holidays);

        return holidays;

    }
    
    // api로 응답받은 item을 Holiday로 변환
    private Holiday convertToEntity(HolidayItemDTO itemDTO) throws RuntimeException {
        // 1. Integer(20250505)를 String으로 변환
        String dateString = String.valueOf(itemDTO.getLocdate());

        // 2. String(YYYYMMDD)을 LocalDate로 변환
        LocalDate locdate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyyMMdd"));

        return Holiday.builder()
            .dateName(itemDTO.getDateName())
            .locdate(locdate)
            .isHoliday(itemDTO.getIsHoliday().charAt(0))
            .build();
    }
    
    // 이미 존재하는 당해년도 공휴일 데이터를 삭제한 뒤, 당해년도 공휴일 데이터와 그 다음년도 공휴일 데이터를 저장
    public void updateHolidaysForYear(Integer year) throws Exception {

        // 기존 데이터 삭제
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        holidayRepository.deleteByLocdateBetween(startDate, endDate);

        // 새 데이터 저장
        List<Holiday> holidays = fetchHoliday(year);

        if (holidays.isEmpty()) return;

        for (Holiday holiday : holidays) {
            if (!holidayRepository.existsByLocdate(holiday.getLocdate())) holidayRepository.save(holiday);
        }

    }
    
    

}
