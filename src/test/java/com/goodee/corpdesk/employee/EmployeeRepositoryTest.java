package com.goodee.corpdesk.employee;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmployeeRepositoryTest {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Test
	void test() {
		Optional<Employee> optional = employeeRepository.findById("user01");
		Employee employee = optional.get();
		System.out.println(employee);
	}

}
