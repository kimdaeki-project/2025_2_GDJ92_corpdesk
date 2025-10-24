package com.goodee.corpdesk.holiday;

import com.goodee.corpdesk.holiday.service.HolidayService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class HolidayTest {

    @Autowired
    HolidayService holidayService;

//    @Test
//    public void testHoliday () throws Exception {
//        holidayService.getHoliday();
//    }

}
