package com.goodee.corpdesk.stats;

import com.goodee.corpdesk.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface StatsRepository extends JpaRepository<Employee, String> {

	@NativeQuery("""
	WITH RECURSIVE AllMonths AS (
		SELECT DATE(:start) AS month_start
		UNION ALL
		SELECT month_start + INTERVAL 1 MONTH
		FROM AllMonths
		WHERE month_start <= :end - INTERVAL 1 MONTH
	)
	SELECT DATE_FORMAT(am.month_start, '%Y-%m') AS date
	FROM AllMonths am
	GROUP BY YEAR(am.month_start), MONTH(am.month_start)
	ORDER BY am.month_start
""")
	List<String> findAllDateMonth(LocalDate start, LocalDate end);

	// 입사자 통계
	@NativeQuery("""
	WITH RECURSIVE AllMonths AS (
		SELECT DATE(:start) AS month_start
		UNION ALL
		SELECT month_start + INTERVAL 1 MONTH
		FROM AllMonths
		WHERE month_start <= :end - INTERVAL 1 MONTH
	)
	SELECT COUNT(e.hire_date) AS count
	FROM AllMonths am
	LEFT JOIN employee e ON DATE_FORMAT(am.month_start, '%Y-%m') = DATE_FORMAT(e.hire_date, '%Y-%m')
	AND (:departmentId IS NULL OR e.department_id = :departmentId)
	AND (:positionId IS NULL OR e.position_id = :positionId)
	GROUP BY YEAR(am.month_start), MONTH(am.month_start)
	ORDER BY am.month_start
""")
	List<Long> countAllJoiner(LocalDate start, LocalDate end, Integer departmentId, Integer positionId);

	// 퇴사자 통계
	@NativeQuery("""
	WITH RECURSIVE AllMonths AS (
		SELECT DATE(:start) AS month_start
		UNION ALL
		SELECT month_start + INTERVAL 1 MONTH
		FROM AllMonths
		WHERE month_start <= :end - INTERVAL 1 MONTH
	)
	SELECT COUNT(e.last_working_day) AS count
	FROM AllMonths am
	LEFT JOIN employee e ON DATE_FORMAT(am.month_start, '%Y-%m') = DATE_FORMAT(e.last_working_day, '%Y-%m')
	AND (:departmentId IS NULL OR e.department_id = :departmentId)
	AND (:positionId IS NULL OR e.position_id = :positionId)
	GROUP BY YEAR(am.month_start), MONTH(am.month_start)
	ORDER BY am.month_start
""")
	List<Long> countAllResigner(LocalDate start, LocalDate end, Integer departmentId, Integer positionId);

	// 재직자 통계
	@NativeQuery("""
	WITH RECURSIVE AllMonths AS (
		SELECT DATE(:start) AS month_start
		UNION ALL
		SELECT month_start + INTERVAL 1 MONTH
		FROM AllMonths
		WHERE month_start <= :end - INTERVAL 1 MONTH
	)
	SELECT COUNT(e.username) AS count
	FROM AllMonths am
	LEFT JOIN employee e ON DATE_FORMAT(am.month_start, '%Y-%m') >= DATE_FORMAT(e.hire_date, '%Y-%m') AND (e.last_working_day IS NULL OR DATE_FORMAT(am.month_start, '%Y-%m') <= DATE_FORMAT(e.last_working_day, '%Y-%m'))
	AND (:departmentId IS NULL OR e.department_id = :departmentId)
	AND (:positionId IS NULL OR e.position_id = :positionId)
	GROUP BY YEAR(am.month_start), MONTH(am.month_start)
	ORDER BY am.month_start
""")
	List<Long> countAllResider(LocalDate start, LocalDate end, Integer departmentId, Integer positionId);


	// 근속기간 통계
	@NativeQuery("""
	WITH sub AS (
		SELECT username, TIMESTAMPDIFF(YEAR, hire_date, IFNULL(last_working_day, :end)) AS diff
		FROM employee
	)
	SELECT diff, COUNT(diff) AS count
	FROM employee e
	JOIN sub s ON e.username = s.username
	WHERE (:departmentId IS NULL OR e.department_id = :departmentId)
	AND (:positionId IS NULL OR e.position_id = :positionId)
	AND e.hire_date < :end AND (e.last_working_day IS NULL OR e.last_working_day >= :start)
	GROUP BY diff
	HAVING diff IS NOT NULL
	ORDER BY diff ASC
""")
	List<Map<String, Long>> countAllServicePeriod(LocalDate start, LocalDate end, Integer departmentId, Integer positionId);


	// 나이 통계
	@NativeQuery("""
	WITH sub AS (
		SELECT username, TIMESTAMPDIFF(YEAR, birth_date, :end) AS diff
		FROM employee
	)
	SELECT diff, COUNT(diff) AS count
	FROM employee e
	JOIN sub s ON e.username = s.username
	WHERE (:departmentId IS NULL OR e.department_id = :departmentId)
AND (:positionId IS NULL OR e.position_id = :positionId)
	AND e.hire_date < :end AND (e.last_working_day IS NULL OR e.last_working_day >= :start)
	GROUP BY diff
	HAVING diff IS NOT NULL
	ORDER BY diff ASC
""")
	List<Map<String, Long>> countAllAge(LocalDate start, LocalDate end, Integer departmentId, Integer positionId);


	// 근태 통계
	@NativeQuery("""
	WITH RECURSIVE AllMonths AS (
		SELECT DATE(:start) AS month_start
		UNION ALL
		SELECT month_start + INTERVAL 1 MONTH
		FROM AllMonths
		WHERE month_start <= :end - INTERVAL 1 MONTH
	)
	SELECT
		DATE_FORMAT(am.month_start, '%Y-%m') AS month,
		COUNT(CASE WHEN a.use_yn = true AND TIME(a.check_in_date_time) <= :workStartTime THEN 1 END) / COUNT(*) * 100 AS attended_count,
		COUNT(CASE WHEN a.use_yn = true AND TIME(a.check_in_date_time) > :workStartTime THEN 1 END) / COUNT(*) * 100 AS late_count,
		COUNT(CASE WHEN a.use_yn = true AND a.check_in_date_time IS NULL THEN 1 END) / COUNT(*) * 100 AS absent_count
	FROM AllMonths am
	LEFT JOIN attendance a
	ON DATE_FORMAT(am.month_start, '%Y-%m') = DATE_FORMAT(a.created_at, '%Y-%m')
	LEFT JOIN employee e
	ON a.username = e.username
	AND (:departmentId IS NULL OR e.department_id = :departmentId)
	AND (:positionId IS NULL OR e.position_id = :positionId)
	GROUP BY YEAR(am.month_start), MONTH(am.month_start)
	ORDER BY am.month_start
""")
	List<Map<String, Object>> countAllAttendance(LocalDate start, LocalDate end, Integer departmentId, Integer positionId, LocalTime workStartTime);

	// 근무 시간 통계
	@NativeQuery("""
	WITH RECURSIVE AllMonths AS (
		SELECT DATE(:start) AS month_start
		UNION ALL
		SELECT month_start + INTERVAL 1 MONTH
		FROM AllMonths
		WHERE month_start <= :end - INTERVAL 1 MONTH
	)
	SELECT
		DATE_FORMAT(am.month_start, '%Y-%m') AS month,
		COALESCE(AVG(CASE WHEN a.use_yn = true 
		THEN LEAST(TIMESTAMPDIFF(HOUR, a.check_in_date_time, a.check_out_date_time), 8) END), 0) AS fixed,
		COALESCE(AVG(CASE WHEN a.use_yn = true AND TIMESTAMPDIFF(HOUR, a.check_in_date_time, a.check_out_date_time) > 8 
		THEN TIMESTAMPDIFF(HOUR, a.check_in_date_time, a.check_out_date_time) - 8 END), 0) AS overtime
	FROM AllMonths am
	LEFT JOIN attendance a
	ON DATE_FORMAT(am.month_start, '%Y-%m') = DATE_FORMAT(a.check_in_date_time, '%Y-%m')
	LEFT JOIN employee e
	ON a.username = e.username
	AND (:departmentId IS NULL OR e.department_id = :departmentId)
	AND (:positionId IS NULL OR e.position_id = :positionId)
	GROUP BY YEAR(am.month_start), MONTH(am.month_start)
	ORDER BY am.month_start
""")
	List<Map<String, Object>> countAllWorkHours(LocalDate start, LocalDate end, Integer departmentId, Integer positionId);
}















