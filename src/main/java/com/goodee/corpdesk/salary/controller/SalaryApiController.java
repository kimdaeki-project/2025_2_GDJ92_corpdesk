package com.goodee.corpdesk.salary.controller;

import com.goodee.corpdesk.salary.dto.EmployeeSalaryDTO;
import com.goodee.corpdesk.salary.service.SalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/salary")
@RequiredArgsConstructor
public class SalaryApiController {

	private final SalaryService salaryService;

	@PostMapping
	public PagedModel<EmployeeSalaryDTO> list(@RequestBody Map<String, Integer> payload) {
		Pageable pageable = PageRequest.of(payload.get("page") - 1, 10);

		return salaryService.getList(pageable);
	}

	@PostMapping("/{paymentId}")
	public Map<String, Object> detail(@RequestBody Map<String, Long> payload) {
		return salaryService.getDetail(payload.get("paymentId"));
	}
}
