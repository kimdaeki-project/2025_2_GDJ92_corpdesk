package com.goodee.corpdesk.stats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.time.LocalTime;

@SpringBootTest
class StatsRepositoryTest {

	@Autowired
	private StatsRepository statsRepository;

//	@Test
//	void countAllByHireDateYearAndHireDateMonth() {
////		List<Integer> datas = statsRepository.countAllByHireDateYearAndHireDateMonth(LocalDate.parse("2020-10-01"), LocalDate.parse("2023-10-01"));
//		List<Date> months = statsRepository.findAllDateMonth(LocalDate.parse("2020-10-01"), LocalDate.parse("2023-10-01"));
//
//		System.out.println("============================");
//		System.out.println(months);
//		Assertions.assertNotEquals(0, months.size());
//	}

//	@Test
//	void countAllServicePeriod() {
//		List<Long> list = statsRepository.countAllServicePeriod();
//
//		System.out.println("============================");
//		System.out.println(list);
//		Assertions.assertNotEquals(0, list.size());
//	}

	@Test
	void countAllAttendance() {
		List<Map<String, Object>> list = statsRepository.countAllAttendance(LocalDate.parse("2025-10-01"), LocalDate.parse("2025-11-01"), null, null, LocalTime.of(9, 0));
		System.out.println("=======================================");
		System.out.println(list);
		Assertions.assertNotEquals(0, list.size());
	}

	@Test
	void countAllWorkHours() {
		List<Map<String, Object>> list = statsRepository.countAllWorkHours(LocalDate.parse("2025-10-01"), LocalDate.parse("2025-11-01"), null, null);
		System.out.println("=======================================");
		System.out.println(list);
		Assertions.assertNotEquals(0, list.size());
	}
}