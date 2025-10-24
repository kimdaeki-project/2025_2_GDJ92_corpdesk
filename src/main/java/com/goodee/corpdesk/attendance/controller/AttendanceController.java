package com.goodee.corpdesk.attendance.controller;

import com.goodee.corpdesk.attendance.DTO.ResAttendanceDTO;
import com.goodee.corpdesk.attendance.entity.Attendance;
import com.goodee.corpdesk.attendance.service.AttendanceService;
import com.goodee.corpdesk.employee.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/attendance/**")
@Slf4j
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Value("${cat.attendance}")
    private String cat;
    @Autowired
    private EmployeeService employeeService;

    @ModelAttribute("cat")
    public String getCat() {
        return cat;
    }

    /**
     * 특정 직원에 대한 출퇴근 목록 화면을 반환합니다.
     *
     * @return 출퇴근 목록 화면 jsp가 있는 경로
     */
    @GetMapping("list")
    public String list(@AuthenticationPrincipal UserDetails userDetails
                        , @RequestParam(value = "year", required = false) String year
                        , @RequestParam(value = "month", required = false) String month
                        , Model model) throws Exception {
        String username = userDetails.getUsername();

        String selectedYear = null;
        String selectedMonth = null;

        if(year != null) selectedYear =  year;
        if(month != null) selectedMonth = month;

        if("".equals(year)) year = null;
        if("".equals(month)) month = null;

        // 1. 출퇴근 기록 중 출근 날짜가 가장 오래된 년도~현재년도
        List<Integer> yearRange = attendanceService.getYearRangeByEmployee(username);
        Integer currentYear = LocalDateTime.now().getYear();
        Integer currentMonth = LocalDateTime.now().getMonthValue();
//        String currentYearStr = LocalDateTime.now().getYear() + "";
//        String currentMonthStr = LocalDateTime.now().getMonthValue() + "";

        // 2. 현재 상태&출퇴근id&출퇴근일시 조회 - 출퇴근 상태, 출근시간, 퇴근시간
        ResAttendanceDTO currAttd = attendanceService.getCurrentAttendance(username);

        // 3. 통계 데이터 조회 (지각, 조퇴, 결근 횟수, 근무 시간, 근무일수)
        ResAttendanceDTO attCnts = new ResAttendanceDTO();
        ResAttendanceDTO workSummary = new ResAttendanceDTO();

        attCnts = attendanceService.getAttendanceCounts(username, year, month);
        workSummary = attendanceService.getWorkSummary(username, year, month);

        // 4. 상세 기록 목록 (기준: 현재 년도&월)
        List<ResAttendanceDTO> attDatilList = new ArrayList<>();

        attDatilList = attendanceService.getAttendanceDetailList(username, year, month);

        model.addAttribute("yearRange", yearRange);
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("currentMonth", currentMonth);
        model.addAttribute("currAttd", currAttd);
        model.addAttribute("attCnts", attCnts);
        model.addAttribute("workSummary", workSummary);
        model.addAttribute("attDatilList", attDatilList);

        if(selectedYear != null) model.addAttribute("selectedYear", selectedYear);
        if(selectedMonth != null) model.addAttribute("selectedMonth", selectedMonth);

        return "attendance/list";
    }

    @PostMapping("")
    public String add(@AuthenticationPrincipal UserDetails userDetails) throws Exception {

        String username = userDetails.getUsername();
        ResAttendanceDTO result = attendanceService.checkIn(username);

        return "redirect:/attendance/list";

    }

    @PatchMapping("{attendanceId}")
    public String update(@PathVariable("attendanceId") Long attendanceId
                         , @AuthenticationPrincipal UserDetails userDetails) throws Exception {

        String username = userDetails.getUsername();
        ResAttendanceDTO result = attendanceService.checkOut(attendanceId, username);

        return "redirect:/attendance/list";
    }

}