package com.goodee.corpdesk.stats;

import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.department.repository.DepartmentRepository;
import com.goodee.corpdesk.position.entity.Position;
import com.goodee.corpdesk.position.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsService {

	private final StatsRepository statsRepository;
	private final DepartmentRepository departmentRepository;
	private final PositionRepository positionRepository;


	@Value("${attendance.work-hour.start}")
	private String workStartHour;
	@Value("${attendance.work-hour.end}")
	private String workEndHour;

	public List<Department> getDepartmentList() {
		return departmentRepository.findAll();
	}

	public List<Position> getPositionList() {
		return positionRepository.findAll();
	}

	// 입퇴사자 및 재직자 통계
	public Map<String, Object> list(LocalDate start, LocalDate end,
			Integer departmentId, Integer positionId) {
		Map<String, Object> map = new HashMap<>();

		List<String> months = statsRepository.findAllDateMonth(start, end);

		List<Long> joiner = statsRepository.countAllJoiner(start, end, departmentId, positionId);
		List<Long> resigner = statsRepository.countAllResigner(start, end, departmentId, positionId);
		List<Long> resider = statsRepository.countAllResider(start, end, departmentId, positionId);
		
		map.put("months", months);
		map.put("joiner", joiner);
		map.put("resigner", resigner);
		map.put("resider", resider);

		return map;
	}

	// 근속기간 통계
	public Map<String, List> list2(LocalDate start, LocalDate end,
			Integer departmentId, Integer positionId) {
		List<Map<String, Long>> list = statsRepository.countAllServicePeriod(start, end, departmentId, positionId);

		List<String> diff = new ArrayList<>() {{
			add("1년 미만");
			add("1 ~ 3년");
			add("3 ~ 5년");
			add("5년 이상");
		}};

		List<Long> count = new ArrayList<>() {{
			add(0L);
			add(0L);
			add(0L);
			add(0L);
		}};

		list.forEach(map -> {
			if(map.get("diff") < 1) count.set(0, count.get(0) + map.get("count"));
			else if(map.get("diff") < 3) count.set(1, count.get(1) + map.get("count"));
			else if(map.get("diff") < 5) count.set(2, count.get(2) + map.get("count"));
			else count.set(3, count.get(3) + map.get("count"));
		});

		return new HashMap<String, List>() {{
			put("diff", diff);
			put("count", count);
		}};
	}

	// 나이 통계
	public Map<String, List> list3(LocalDate start, LocalDate end,
			Integer departmentId, Integer positionId) {
		List<Map<String, Long>> list = statsRepository.countAllAge(start, end, departmentId, positionId);

		List<String> diff = new ArrayList<>() {{
			add("20 ~ 29세");
			add("30 ~ 39세");
			add("40 ~ 49세");
			add("50세 이상");
		}};

		List<Long> count = new ArrayList<>() {{
			add(0L);
			add(0L);
			add(0L);
			add(0L);
		}};

		list.forEach(map -> {
			if(map.get("diff") >= 20 && map.get("diff") < 30) count.set(0, count.get(0) + map.get("count"));
			else if(map.get("diff") >= 30 && map.get("diff") < 40) count.set(1, count.get(1) + map.get("count"));
			else if(map.get("diff") >= 40 && map.get("diff") < 50) count.set(2, count.get(2) + map.get("count"));
			else if(map.get("diff") >= 50) count.set(3, count.get(3) + map.get("count"));
		});

		return new HashMap<String, List>() {{
			put("diff", diff);
			put("count", count);
		}};
	}

	// 근태 통계
	public Map<String, List> list4(LocalDate start, LocalDate end,
								   Integer departmentId, Integer positionId) {
		LocalTime workStartTime = LocalTime.parse(workStartHour);

		List<Map<String, Object>> list = statsRepository.countAllAttendance(start, end, departmentId, positionId, workStartTime);

		List<String> months = new ArrayList<>();

		List<Number> attended = new ArrayList<>();
		List<Number> late = new ArrayList<>();
		List<Number> absent = new ArrayList<>();

		list.forEach(map -> {
			months.add((String) map.get("month"));

			attended.add(((Number) map.get("attended_count")).doubleValue());
			late.add(((Number) map.get("late_count")).doubleValue());
			absent.add(((Number) map.get("absent_count")).doubleValue());
		});

		return new HashMap<String, List>() {{
			put("months", months);

			put("attended", attended);
			put("late", late);
			put("absent", absent);
		}};
	}

	// 근무 시간 통계
	public Map<String, List> list5(LocalDate start, LocalDate end,
								   Integer departmentId, Integer positionId) {
		List<Map<String, Object>> list = statsRepository.countAllWorkHours(start, end, departmentId, positionId);

		List<String> months = new ArrayList<>();

		List<Long> fixed = new ArrayList<>();
		List<Long> overtime = new ArrayList<>();

		list.forEach(map -> {
			months.add((String) map.get("month"));

			fixed.add(((Number) map.get("fixed")).longValue());
			overtime.add(((Number) map.get("overtime")).longValue());
		});

		return new HashMap<String, List>() {{
			put("months", months);

			put("fixed", fixed);
			put("overtime", overtime);
		}};
	}
}
