package com.goodee.corpdesk.holiday;

import com.goodee.corpdesk.holiday.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class HolidayScheduler {

    @Autowired
    private HolidayService holidayService;

    @Scheduled(cron = "0 0 2 1 1 ?")
    public void updateHolidaysAnnually() throws Exception {
        Integer currYear = LocalDate.now().getYear();

        holidayService.updateHolidaysForYear(currYear);
    }

}
