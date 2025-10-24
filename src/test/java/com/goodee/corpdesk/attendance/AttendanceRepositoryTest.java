package com.goodee.corpdesk.attendance;

import com.goodee.corpdesk.attendance.DTO.ResAttendanceDTO;
import com.goodee.corpdesk.attendance.entity.Attendance;
import com.goodee.corpdesk.attendance.repository.AttendanceRepository;
import com.goodee.corpdesk.attendance.service.AttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@SpringBootTest
@Slf4j
class AttendanceRepositoryTest {

    @Autowired
    AttendanceService attendanceService;
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Value("${attendance.work-hour.start}")
    private String startHour;
    @Value("${attendance.work-hour.end}")
    private String endHour;

    @Test
    void countByCheckInDateTimeTimeAfterAndUsername_Success() {
        // when
        long lateCount = attendanceRepository.countLateArrivalsByUsernameAndYearMonth(LocalTime.parse(startHour), "jung_frontend", null, null);

        // then
        log.warn("{}",lateCount);
    }

    // ============================= 지각 횟수 조회 테스트

    @Test
    @DisplayName("특정 년월 조회 - 월 파라미터가 있는 경우")
    void countLateAttendanceByUsernameAndYearMonth_WithMonth_Success() {
        // given
        LocalTime baseTime = LocalTime.parse(startHour);
        String username = "jung_frontend";
        String year = "2025";
        String month = "9";  // 9월만 조회

        // when
        long lateCountWithMonth = attendanceRepository.countLateArrivalsByUsernameAndYearMonth(
                baseTime, username, year, month);

        // then
        log.warn("특정 월({}월) 지각 횟수: {}", month, lateCountWithMonth);
    }

    @Test
    @DisplayName("특정 년월 조회 - 월 파라미터가 null인 경우")
    void countLateAttendanceByUsernameAndYearMonth_WithoutMonth_Success() {
        // given
        LocalTime baseTime = LocalTime.of(9, 0);
        String username = "jung_frontend";
        String year = "2025";
        String month = null;  // 전체 월 조회

        // when
        long lateCountAllMonths = attendanceRepository.countLateArrivalsByUsernameAndYearMonth(
                baseTime, username, year, month);

        // then
        log.warn("전체 월 지각 횟수: {}", lateCountAllMonths);
    }

    // ============================= 결근 횟수 조회 테스트
    @Test
    @DisplayName("특정 직원의 전체 결근 횟수 조회")
    void countAbsentDaysByUsername_Success() {
        // given
        String username = "jung_frontend";

        // when
        long absentDaysCount = attendanceRepository.countAbsentDaysByUsernameAndYearMonth(username, null, null);

        // then
        log.warn("{}의 전체 결근 횟수: {}", username, absentDaysCount);
    }

    @Test
    @DisplayName("특정 직원의 년/월별 결근 횟수 조회 - 특정 월")
    void countAbsentDaysByUsernameAndYearMonth_WithMonth_Success() {
        // given
        String username = "jung_frontend";
        String year = "2025";
        String month = "9";

        // when
        long absentDaysCount = attendanceRepository.countAbsentDaysByUsernameAndYearMonth(username, year, month);

        // then
        log.warn("{}의 {}년 {}월 결근 횟수: {}", username, year, month, absentDaysCount);
    }

    @Test
    @DisplayName("특정 직원의 년/월별 결근 횟수 조회 - 전체 월")
    void countAbsentDaysByUsernameAndYearMonth_WithoutMonth_Success() {
        // given
        String username = "jung_frontend";
        String year = "2025";
        String month = null;  // 전체 월

        // when
        long absentDaysCount = attendanceRepository.countAbsentDaysByUsernameAndYearMonth(username, year, month);

        // then
        log.warn("{}의 {}년 전체 결근 횟수: {}", username, year, absentDaysCount);
    }

    // ============================= 조퇴 횟수 조회 테스트
    @Test
    @DisplayName("특정 직원의 전체 조퇴 횟수 조회")
    void countEarlyLeavingsByUsername_Success() {
        // given
        LocalTime earlyLeaveTime = LocalTime.parse(endHour);  // 6시 이전 퇴근을 조퇴로 간주
        String username = "jung_frontend";

        // when
        long earlyLeavingsCount = attendanceRepository.countEarlyLeavingsByUsernameAndYearMonth(earlyLeaveTime, username, null, null);

        // then
        log.warn("{}의 전체 조퇴 횟수 ({}시 이전 퇴근): {}", username, earlyLeaveTime, earlyLeavingsCount);
    }

    @Test
    @DisplayName("특정 직원의 년/월별 조퇴 횟수 조회 - 특정 월")
    void countEarlyLeavingsByUsernameAndYearMonth_WithMonth_Success() {
        // given
        LocalTime earlyLeaveTime = LocalTime.parse(endHour);
        String username = "jung_frontend";
        String year = "2025";
        String month = "9";

        // when
        long earlyLeavingsCount = attendanceRepository.countEarlyLeavingsByUsernameAndYearMonth(earlyLeaveTime, username, year, month);

        // then
        log.warn("{}의 {}년 {}월 조퇴 횟수: {}", username, year, month, earlyLeavingsCount);
    }

    @Test
    @DisplayName("특정 직원의 년/월별 조퇴 횟수 조회 - 전체 월")
    void countEarlyLeavingsByUsernameAndYearMonth_WithoutMonth_Success() {
        // given
        LocalTime earlyLeaveTime = LocalTime.parse(endHour);
        String username = "jung_frontend";
        String year = "2025";
        String month = null;  // 전체 월

        // when
        long earlyLeavingsCount = attendanceRepository.countEarlyLeavingsByUsernameAndYearMonth(
                earlyLeaveTime, username, year, month);

        // then
        log.warn("{}의 {}년 전체 조퇴 횟수: {}", username, year, earlyLeavingsCount);
    }

    // ============================= 근무 집계 데이터 조회 테스트
    @Test
    @DisplayName("특정 직원의 전체 근무 집계 데이터 조회")
    void findWorkSummaryByUsernameAndYearMonth_AllData_Success() {
        // given
        String username = "jung_frontend";

        // when
        ResAttendanceDTO workSummary = attendanceRepository.findWorkSummaryByUsernameAndYearMonth(
                username, null, null, LocalDateTime.now());

        // then
        log.warn("{}의 전체 근무 집계:", username);
        log.warn("  총 근무시간: {}시간", workSummary.getTotalWorkHours());
        log.warn("  총 근무일수: {}일", workSummary.getTotalWorkDays());
    }

    @Test
    @DisplayName("특정 직원의 년별 근무 집계 데이터 조회")
    void findWorkSummaryByUsernameAndYearMonth_YearData_Success() {
        // given
        String username = "jung_frontend";
        String year = "2025";

        // when
        ResAttendanceDTO workSummary = attendanceRepository.findWorkSummaryByUsernameAndYearMonth(
                username, year, null, LocalDateTime.now());

        // then
        log.warn("{}의 {}년 근무 집계:", username, year);
        log.warn("  총 근무시간: {}시간", workSummary.getTotalWorkHours());
        log.warn("  총 근무일수: {}일", workSummary.getTotalWorkDays());
    }

    @Test
    @DisplayName("특정 직원의 월별 근무 집계 데이터 조회")
    void findWorkSummaryByUsernameAndYearMonth_MonthData_Success() {
        // given
        String username = "jung_frontend";
        String year = "2025";
        String month = "9";

        // when
        ResAttendanceDTO workSummary = attendanceRepository.findWorkSummaryByUsernameAndYearMonth(
                username, year, month, LocalDateTime.now());

        // then
        log.warn("{}의 {}년 {}월 근무 집계:", username, year, month);
        log.warn("  총 근무시간: {}시간", workSummary.getTotalWorkHours());
        log.warn("  총 근무일수: {}일", workSummary.getTotalWorkDays());
    }

    // ============================= 근무 상세 기록 조회 테스트
    @Test
    @DisplayName("특정 직원의 전체 근무 상세 기록 조회 (년, 월 NULL)")
    void findByUseYnAndUsernameAndYearMonth_AllData_Success() {
        // given
        String username = "jung_frontend";

        // when
        List<Attendance> attendances = attendanceRepository.findByUseYnAndUsernameAndYearMonth(
                username, null, null);

        // then
        log.warn("{}의 전체 근무 상세 기록 개수: {}", username, attendances.size());
        if (!attendances.isEmpty()) {
            log.warn("  첫 번째 기록: 출근일시={}, 퇴근일시={}, 상태={}",
                    attendances.get(0).getCheckInDateTime(),
                    attendances.get(0).getCheckOutDateTime(),
                    attendances.get(0).getWorkStatus());
        }
    }

    @Test
    @DisplayName("특정 직원의 년별 근무 상세 기록 조회 (월 NULL)")
    void findByUseYnAndUsernameAndYearMonth_YearData_Success() {
        // given
        String username = "jung_frontend";
        String year = "2025";
        String month = null;

        // when
        List<Attendance> attendances = attendanceRepository.findByUseYnAndUsernameAndYearMonth(
                username, year, month);

        // then
        log.warn("{}의 {}년 근무 상세 기록 개수: {}", username, year, attendances.size());
        if (!attendances.isEmpty()) {
            log.warn("  첫 번째 기록: 출근일시={}, 퇴근일시={}, 상태={}",
                    attendances.get(0).getCheckInDateTime(),
                    attendances.get(0).getCheckOutDateTime(),
                    attendances.get(0).getWorkStatus());
        }
    }

    @Test
    @DisplayName("특정 직원의 년/월별 근무 상세 기록 조회")
    void findByUseYnAndUsernameAndYearMonth_YearMonthData_Success() {
        // given
        String username = "jung_frontend";
        String year = "2025";
        String month = "9";

        // when
        List<Attendance> attendances = attendanceRepository.findByUseYnAndUsernameAndYearMonth(
                username, year, month);

        // then
        log.warn("{}의 {}년 {}월 근무 상세 기록 개수: {}", username, year, month, attendances.size());
        if (!attendances.isEmpty()) {
            log.warn("  첫 번째 기록: 출근일시={}, 퇴근일시={}, 상태={}",
                    attendances.get(0).getCheckInDateTime(),
                    attendances.get(0).getCheckOutDateTime(),
                    attendances.get(0).getWorkStatus());
        }
    }


	@Test
	void findAllByUsernameAndMonth() {
		List<Attendance> list = attendanceRepository.findAllByUsernameAndMonth("choi-sales");
		int count = 0;

		for (Attendance a : list) {
			LocalDateTime checkIn = a.getCheckInDateTime();
			LocalDateTime checkOut = a.getCheckOutDateTime();

			System.out.println("=======================================");
			if (!checkIn.toLocalDate().isEqual(checkOut.toLocalDate())
					|| checkIn.isBefore(checkIn.withHour(06).withMinute(00).withSecond(00))
					|| checkOut.isAfter(checkOut.withHour(22).withMinute(00).withSecond(00))) {
				System.out.println(a);
				System.out.println(Duration.between(checkIn, checkOut).toMinutes() + " minutes");
				count++;
			}
		}

		Assertions.assertNotEquals(0, count);
	}

	@Test
	void isAfter() {
		System.out.println(LocalTime.parse("10:00:00") == LocalTime.parse("10:00:00"));
		Assertions.assertTrue(LocalTime.parse("10:00:00") == LocalTime.parse("10:00:00"));
	}

	@Test
	void between() {
		System.out.println("=======================================");
		System.out.println(Duration.between(LocalTime.parse("22:00:00"), LocalTime.parse("06:00:00")).toMinutes());
	}
}