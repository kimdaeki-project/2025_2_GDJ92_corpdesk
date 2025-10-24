package com.goodee.corpdesk.salary.scheduler;

import com.goodee.corpdesk.employee.EmployeeService;
import com.goodee.corpdesk.salary.service.SalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SalaryScheduler {

	private final EmployeeService employeeService;
	private final SalaryService salaryService;

	@Scheduled(cron = "${salary.cron.payday}")
	public void pay() {
		salaryService.saveSalaries();
	}
}
