package com.goodee.corpdesk.schedule.controller;

import com.goodee.corpdesk.schedule.dto.ReqPersonalScheduleDTO;
import com.goodee.corpdesk.schedule.dto.ResPersonalScheduleDTO;
import com.goodee.corpdesk.schedule.repository.PersonalScheduleRepository;
import com.goodee.corpdesk.schedule.service.PersonalScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/personal-schedule/**")
@Slf4j
public class PersonalScheduleController {

    @Value("${cat.schedule}")
    private String cat;

    @ModelAttribute("cat")
    public String getCat() {
        return cat;
    }

    @Autowired
    private PersonalScheduleService personalScheduleService;

//    @ModelAttribute("todaySchedules")
//    @GetMapping("today")
//    @ResponseBody
//    public List<ResPersonalScheduleDTO> getTodaySchedules(@AuthenticationPrincipal UserDetails userDetails) {
//
//        String username = userDetails.getUsername();
//
//        LocalDate today = LocalDate.now();
//        LocalDateTime startOfDay = today.atStartOfDay();
//        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
//
//        List<ResPersonalScheduleDTO> list = personalScheduleService.getSchedulesByDate(username, startOfDay, endOfDay);
//        log.warn("todaySchedules: {}", list);
//
//        return list;
//
//    }

    @GetMapping("list")
    public String list(@AuthenticationPrincipal UserDetails userDetails
                       , ReqPersonalScheduleDTO reqPersonalScheduleDTO
                       , Model model) {

        // username, useYn, (year, month)로 일정 데이터들 조회
        String username = userDetails.getUsername();
        List<ResPersonalScheduleDTO> schedules = personalScheduleService.getSchedules(username, reqPersonalScheduleDTO);

        // yearRange 생성
        List<Integer> yearRange = personalScheduleService.getYearRangeByUsername(username);

        model.addAttribute("schedules", schedules);
        model.addAttribute("yearRange", yearRange);
        model.addAttribute("selectedYear", reqPersonalScheduleDTO.getYear());
        model.addAttribute("selectedMonth", reqPersonalScheduleDTO.getMonth());

        return "schedule/list";
    }

    @PostMapping("")
    public String add(@AuthenticationPrincipal UserDetails userDetails, ReqPersonalScheduleDTO reqPersonalScheduleDTO) {

        String username = userDetails.getUsername();
        ResPersonalScheduleDTO newSchedule = personalScheduleService.createSchedule(username, reqPersonalScheduleDTO);

        // redirect 상세정보 페이지
        // TODO 반환타입을 void->String으로 변경 후, 상세정보 페이지를 return하는 코드 추가

        log.warn("ResPersonalScheduleDTO:{}", newSchedule);

        return "redirect:/personal-schedule/list";

    }

    @GetMapping("{personalScheduleId}")
    public String detail(@PathVariable("personalScheduleId") Long personalScheduleId, Model model) {

        // id로 상세정보 조회해옴
        ResPersonalScheduleDTO schedule = personalScheduleService.getScheduleById(personalScheduleId);

        model.addAttribute("schedule", schedule);

        return "schedule/detail";
    }

    @GetMapping("{personalScheduleId}/edit")
    public String edit(@PathVariable("personalScheduleId") Long personalScheduleId, Model model) {

        // id로 조회
        ResPersonalScheduleDTO schedule = personalScheduleService.getScheduleById(personalScheduleId);

        // 조회해온 데이터를 model에 바인딩한 후 수정 폼으로 이동
        model.addAttribute("schedule", schedule);

        return "schedule/edit";

    }

    @PutMapping("{personalScheduleId}")
    public String update(@AuthenticationPrincipal UserDetails userDetails
                        , @PathVariable("personalScheduleId") Long personalScheduleId
                        , ReqPersonalScheduleDTO reqPersonalScheduleDTO) {

        // service의 수정 로직 (id로 조회 -> save)
        String modifiedBy = userDetails.getUsername();
        personalScheduleService.updateSchedule(modifiedBy, personalScheduleId, reqPersonalScheduleDTO);

        // 상세정보 페이지로 redirect
        return "redirect:/personal-schedule/" + personalScheduleId;

    }

    @DeleteMapping("{personalScheduleId}")
    public String delete(@AuthenticationPrincipal UserDetails userDetails
                         , @PathVariable("personalScheduleId") Long personalScheduleId) {

        // service의 삭제 로직
        String modifiedBy = userDetails.getUsername();
        personalScheduleService.deleteSchedule(modifiedBy, personalScheduleId);

        // list 페이지로 redirect
        return "redirect:/personal-schedule/list";

    }

    // 공통 로직
    private List<ResPersonalScheduleDTO> fetchTodaySchedules(String username) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        return personalScheduleService.getSchedulesByDate(username, startOfDay, endOfDay);
    }

    // @ModelAttribute용
    @ModelAttribute("todaySchedules")
    public List<ResPersonalScheduleDTO> addTodaySchedulesToModel(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return List.of();
        }
        return fetchTodaySchedules(userDetails.getUsername());
    }

    // API용
    @GetMapping("today")
    @ResponseBody
    public List<ResPersonalScheduleDTO> getTodaySchedulesApi(@AuthenticationPrincipal UserDetails userDetails) throws Exception {

        List<ResPersonalScheduleDTO> schedules = fetchTodaySchedules(userDetails.getUsername());

        return personalScheduleService.bindGeocodesToSchedules(schedules);
    }

}
