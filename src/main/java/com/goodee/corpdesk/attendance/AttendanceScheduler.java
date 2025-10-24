package com.goodee.corpdesk.attendance;

import com.goodee.corpdesk.attendance.service.AttendanceService;
import com.goodee.corpdesk.holiday.service.HolidayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j
public class AttendanceScheduler {

    @Autowired
    private AttendanceService attendanceService;

    @Scheduled(cron = "0 0 2 * * ?")
    public void recordAbsencesDaily() throws Exception {

        // 어제가 주말이거나 공휴일이었다면 실행하지 않음
        Boolean isHoliday = false;
        LocalDate yesterday = LocalDate.now().minusDays(1);
        log.warn("yesterday: {}", yesterday);

        // 1) 어제가 공휴일인지 구함
        // TODO 특일 api 사용 가능해지면 구현

        // 2) 어제가 주말인지 구함
        if(yesterday.getDayOfWeek().getValue() >= 6) isHoliday = true;
        log.warn("isHoliday: {}", isHoliday);

        if(isHoliday) return;

        attendanceService.recordAbsence();
    }

}
