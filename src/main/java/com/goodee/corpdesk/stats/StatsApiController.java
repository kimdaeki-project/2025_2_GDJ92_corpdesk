package com.goodee.corpdesk.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsApiController {

	private final StatsService statsService;

	// 입퇴사자 및 재직자 통계
	@PostMapping("/chart1")
	public Map<String, Object> list(@RequestBody Map<String, Object> payload) {
		LocalDate end = LocalDate.now();
		LocalDate start = end.minusYears(1);
		if (payload.get("start") != null && !payload.get("start").equals("")) {
			start = LocalDate.parse((String) payload.get("start"));
		}
		if (payload.get("end") != null && !payload.get("end").equals("")) {
			end = LocalDate.parse((String) payload.get("end"));
		}

		Integer departmentId = (Integer) payload.get("departmentId");
		Integer positionId = (Integer) payload.get("positionId");

		return statsService.list(start, end, departmentId, positionId);
	}

	// 근속기간 통계
	@PostMapping("/chart2")
	public Map<String, List> list2(@RequestBody Map<String, Object> payload) {
		LocalDate end = LocalDate.now();
		LocalDate start = end.minusYears(1);
		if (payload.get("start") != null && !payload.get("start").equals("")) {
			start = LocalDate.parse((String) payload.get("start"));
		}
		if (payload.get("end") != null && !payload.get("end").equals("")) {
			end = LocalDate.parse((String) payload.get("end"));
		}

		Integer departmentId = (Integer) payload.get("departmentId");
		Integer positionId = (Integer) payload.get("positionId");

		return statsService.list2(start, end, departmentId, positionId);
	}

	// 나이 통계
	@PostMapping("/chart3")
	public Map<String, List> list3(@RequestBody Map<String, Object> payload) {
		LocalDate end = LocalDate.now();
		LocalDate start = end.minusYears(1);
		if (payload.get("start") != null && !payload.get("start").equals("")) {
			start = LocalDate.parse((String) payload.get("start"));
		}
		if (payload.get("end") != null && !payload.get("end").equals("")) {
			end = LocalDate.parse((String) payload.get("end"));
		}

		Integer departmentId = (Integer) payload.get("departmentId");
		Integer positionId = (Integer) payload.get("positionId");

		return statsService.list3(start, end, departmentId, positionId);
	}

	// 근태 통계
	@PostMapping("/chart4")
	public Map<String, List> list4(@RequestBody Map<String, Object> payload) {
		LocalDate end = LocalDate.now();
		LocalDate start = end.minusYears(1);
		if (payload.get("start") != null && !payload.get("start").equals("")) {
			start = LocalDate.parse((String) payload.get("start"));
		}
		if (payload.get("end") != null && !payload.get("end").equals("")) {
			end = LocalDate.parse((String) payload.get("end"));
		}

		Integer departmentId = (Integer) payload.get("departmentId");
		Integer positionId = (Integer) payload.get("positionId");

		return statsService.list4(start, end, departmentId, positionId);
	}

	// 근무 시간 통계
	@PostMapping("/chart5")
	public Map<String, List> list5(@RequestBody Map<String, Object> payload) {
		LocalDate end = LocalDate.now();
		LocalDate start = end.minusYears(1);
		if (payload.get("start") != null && !payload.get("start").equals("")) {
			start = LocalDate.parse((String) payload.get("start"));
		}
		if (payload.get("end") != null && !payload.get("end").equals("")) {
			end = LocalDate.parse((String) payload.get("end"));
		}

		Integer departmentId = (Integer) payload.get("departmentId");
		Integer positionId = (Integer) payload.get("positionId");

		return statsService.list5(start, end, departmentId, positionId);
	}
}
