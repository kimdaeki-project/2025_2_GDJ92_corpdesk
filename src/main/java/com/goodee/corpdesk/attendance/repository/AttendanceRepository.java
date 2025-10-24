package com.goodee.corpdesk.attendance.repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goodee.corpdesk.attendance.DTO.ResAttendanceDTO;
import com.goodee.corpdesk.attendance.entity.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

//    // 기간 내 모든 근태
//    List<Attendance> findAllByCheckInDatetimeBetween(LocalDateTime start, LocalDateTime end);
//
//    // 직원별 + 기간 내 근태 (필요시 사용)
//    List<Attendance> findAllByEmployeeIdAndCheckInDatetimeBetween(Long employeeId, LocalDateTime start, LocalDateTime end);

	List<Attendance> findByUsernameAndUseYn(String username, Boolean useYn);
	// username의 가장 최근 출근시각 1건
    Optional<Attendance> findFirstByUsernameAndCheckInDateTimeIsNotNullOrderByCheckInDateTimeDesc(String username);
    // 아직 퇴근 처리 안 된 가장 최근 레코드
    Optional<Attendance> findTopByUsernameAndCheckOutDateTimeIsNullOrderByCheckInDateTimeDesc(String username);
    
    // 목록
    List<Attendance> findByUsernameOrderByCheckInDateTimeDesc(String username);
    
    // 열린 근무(퇴근 미처리) 1건
    Optional<Attendance> findTopByUsernameAndUseYnTrueAndCheckInDateTimeIsNotNullAndCheckOutDateTimeIsNullOrderByCheckInDateTimeDesc(String username);
    
    
    
    @Modifying
    @Query("UPDATE Attendance a SET a.useYn = false WHERE a.attendanceId IN :ids")
    void softDeleteByIds(@Param("ids") List<Long> ids);

    // ======================== passing7by
    // 특정 직원의 가장 최근 출퇴근 기록 조회
    @NativeQuery("""
        SELECT *
        FROM attendance
        WHERE
            use_yn = true
        	AND check_in_date_time = (SELECT MAX(check_in_date_time)
                                      FROM attendance
                                      WHERE
                                          use_yn = true
                                          AND username = :username)
        	AND username  = :username
    """)
    Attendance findLatestAttendanceByUsername(@Param("username") String username);

    // 특정 직원의 가장 오래된 출퇴근 기록에서 출근년도 조회
    @NativeQuery("""
        SELECT MIN(YEAR(check_in_date_time))
        FROM attendance
        WHERE
            use_yn = true
        	AND username  = :username
    """)
    Integer findOldestCheckInYearByUsername(@Param("username") String username);

    // 특정 직원의 지각 횟수 조회 - 전체/년/월/년&월
    @NativeQuery("""
        SELECT COUNT(attendance_id)
        FROM attendance
        WHERE
            use_yn = true
        	AND TIME(check_in_date_time) > :time
        	AND username = :username
        	AND (:year IS NULL OR YEAR(check_in_date_time) = :year)
         	AND (:month IS NULL OR MONTH(check_in_date_time) = :month)
    """)
    long countLateArrivalsByUsernameAndYearMonth(@Param("time") LocalTime time
                                                    , @Param("username") String username
                                                    , @Param("year") String year
                                                    , @Param("month") String month);

    // 특정 직원의 결근 횟수 조회 - 전체/년/월/년&월
    @NativeQuery("""
        SELECT COUNT(attendance_id)
        FROM attendance
        WHERE
            use_yn = true
            AND work_status = '결근'
            AND username = :username
            AND (:year IS NULL OR YEAR(DATE(created_at) - INTERVAL 1 DAY) = :year)
            AND (:month IS NULL OR MONTH(DATE(created_at) - INTERVAL 1 DAY) = :month)
    """)
    long countAbsentDaysByUsernameAndYearMonth(@Param("username") String username,
                                               @Param("year") String year,
                                               @Param("month") String month);

    // 특정 직원의 조퇴 횟수 조회 - 전체/년/월/년&월
    @NativeQuery("""
        SELECT COUNT(attendance_id)
        FROM attendance
        WHERE
            use_yn = true
            AND TIME(check_out_date_time) < :time
            AND username = :username
	        AND (:year IS NULL OR YEAR(check_in_date_time) = :year)
	        AND (:month IS NULL OR MONTH(check_in_date_time) = :month)
    """)
    long countEarlyLeavingsByUsernameAndYearMonth(@Param("time") LocalTime time,
                                                  @Param("username") String username,
                                                  @Param("year") String year,
                                                  @Param("month") String month);

    // 특정 직원의 근무 집계 데이터 조회 (총 근무시간, 총 근무일수) - 전체/년/월/년&월
    @NativeQuery("""
        WITH a AS (
            SELECT
                DATE(check_in_date_time) AS workDate
               , SUM(TIMESTAMPDIFF(HOUR, check_in_date_time, COALESCE(check_out_date_time, :now))) AS workHours
               , MAX(TIMESTAMPDIFF(DAY, check_in_date_time - INTERVAL 1 DAY, COALESCE(check_out_date_time, :now)))
                  AS workDays
               , check_in_date_time
               , check_out_date_time
            FROM attendance a
            WHERE
                use_yn = true
                AND username = :username
                AND (:year IS NULL OR YEAR(check_in_date_time) = :year)
                AND (:month IS NULL OR MONTH(check_in_date_time) = :month)
            GROUP BY DATE(check_in_date_time)
        )
        SELECT SUM(a.workHours) AS totalWorkHours, SUM(a.workDays) AS totalWorkDays
        FROM a
    """)
    ResAttendanceDTO findWorkSummaryByUsernameAndYearMonth(@Param("username") String username,
                                                           @Param("year") String year,
                                                           @Param("month") String month,
                                                           @Param("now") LocalDateTime now);

    // 특정 직원의 근무 상세 기록 조회 (출근일, 출근시간, 퇴근일, 퇴근시간, 근무상태) - 전체/년/월/년&월
    @NativeQuery("""
        SELECT *
        FROM attendance
        WHERE
        	use_yn = TRUE
        	AND username = :username
            AND (:year IS NULL OR YEAR(check_in_date_time) = :year)
            AND (:month IS NULL OR MONTH(check_in_date_time) = :month)
        ORDER BY check_in_date_time DESC
    """)
    List<Attendance> findByUseYnAndUsernameAndYearMonth(@Param("username") String username,
                                                        @Param("year") String year,
                                                        @Param("month") String month);

    // ======================== A1ueo
	// 급여
	// FIXME: 정상 퇴근만 가져오기?
	@NativeQuery("""
				SELECT *
				FROM attendance
				WHERE use_yn = true
				AND username = :username
				AND YEAR(check_in_date_time) = YEAR(NOW() - INTERVAL 1 MONTH)
				AND MONTH(check_in_date_time) = MONTH(NOW() - INTERVAL 1 MONTH)
			""")
	List<Attendance> findAllByUsernameAndMonth(String username);

	// 캘린더
	@Query("""
	SELECT a
	FROM Attendance a
	WHERE a.username = :username
	AND a.useYn = true
	AND (
		a.checkInDateTime <= :end
		AND (
		a.checkOutDateTime IS NULL
		OR
		a.checkOutDateTime >= :start
		)
	)
""")
	List<Attendance> findAllByUsernameAndDateTime(String username, LocalDateTime start, LocalDateTime end);
}
