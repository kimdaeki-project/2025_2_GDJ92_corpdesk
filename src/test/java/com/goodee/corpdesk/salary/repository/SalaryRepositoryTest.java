package com.goodee.corpdesk.salary.repository;

import com.goodee.corpdesk.salary.dto.EmployeeSalaryDTO;
import com.goodee.corpdesk.salary.entity.Allowance;
import com.goodee.corpdesk.salary.entity.Deduction;
import com.goodee.corpdesk.salary.entity.SalaryPayment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@SpringBootTest
class SalaryRepositoryTest {

	@Autowired
	private SalaryRepository salaryRepository;
	@Autowired
	private AllowanceRepository allowanceRepository;
	@Autowired
	private DeductionRepository deductionRepository;

	@Test
	void findAllEmployeeSalary() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<EmployeeSalaryDTO> page = salaryRepository.findAllEmployeeSalary(pageable);

		System.out.println("===================== " + page.getContent());
	}

	@Test
	void findAll() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<SalaryPayment> list = salaryRepository.findAll(pageable);
		System.out.println("===================== " + list.getContent());
	}

//	@Test
//	void findByIdAndPaymentDate() {
//		SalaryPayment salaryPayment = salaryRepository.findByIdAndPaymentDate("kim-it", LocalDateTime.parse("2024-01-01T00:00:00")).get();
//
//		System.out.println("=======================================");
//		System.out.println(salaryPayment);
//	}

	@Test
	void find() {
		SalaryPayment salaryPayment = salaryRepository.findById(1L).get();
		List<Allowance> allowanceList = allowanceRepository.findAllByPaymentId(1L);
		List<Deduction> deductionList = deductionRepository.findAllByPaymentId(1L);

		System.out.println("===============================");
		System.out.println(salaryPayment);
		System.out.println(allowanceList);
		System.out.println(deductionList);
	}
}