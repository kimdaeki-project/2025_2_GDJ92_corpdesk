package com.goodee.corpdesk.holiday;

import com.goodee.corpdesk.holiday.entity.Holiday;
import com.goodee.corpdesk.holiday.repository.HolidayRepository;
import com.goodee.corpdesk.holiday.service.HolidayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class HolidayServiceTest {

    @Autowired
    private HolidayService holidayService;
    @Autowired
    private HolidayRepository holidayRepository;

    @BeforeEach
    void setUp() {
        // 테스트 전 DB 초기화
        holidayRepository.deleteAll();
    }


    @Test
    @DisplayName("특정 연도의 공휴일 데이터를 성공적으로 저장한다")
    void updateHolidaysForYear_Success() throws Exception {
        // given
        Integer year = 2025;

        // when
        holidayService.updateHolidaysForYear(year);

        // then
        List<Holiday> savedHolidays = holidayRepository.findAll();

        assertFalse(savedHolidays.isEmpty(), "저장된 공휴일이 존재해야 함");

        // 모든 공휴일이 해당 연도인지 확인
        boolean allMatchYear = savedHolidays.stream()
            .allMatch(holiday -> holiday.getLocdate().getYear() == year);
        assertTrue(allMatchYear, "모든 공휴일이 " + year + "년이어야 함");

        // 주요 공휴일이 포함되어 있는지 확인 (예: 신정)
        boolean hasNewYear = savedHolidays.stream()
            .anyMatch(holiday -> holiday.getDateName().contains("1월1일"));
        assertTrue(hasNewYear, "신정이 포함되어야 함");
    }
}
