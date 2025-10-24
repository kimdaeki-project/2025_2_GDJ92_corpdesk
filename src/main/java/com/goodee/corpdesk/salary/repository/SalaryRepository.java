package com.goodee.corpdesk.salary.repository;

import com.goodee.corpdesk.salary.dto.EmployeeSalaryDTO;
import com.goodee.corpdesk.salary.entity.SalaryPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SalaryRepository extends JpaRepository<SalaryPayment, Long> {

	@Query(value = """
			SELECT
					s.payment_id as paymentId,
					s.username as username,
					s.payment_date as paymentDate,
					s.base_salary as baseSalary,
			
					e.name as name,
					e.responsibility as responsibility,
			
					d.department_name as departmentName,
					p.position_name as positionName,
			
					IFNULL(a.allowance_amount, 0) as allowanceAmount,
					IFNULL(de.deduction_amount, 0) as deductionAmount
			FROM salary_payment s
			LEFT JOIN employee e ON s.username = e.username
			LEFT JOIN department d ON e.department_id = d.department_id
			LEFT JOIN position p ON e.position_id = p.position_id
			LEFT JOIN (
					SELECT payment_id, SUM(allowance_amount) as allowance_amount
					FROM allowance
					GROUP BY payment_id
			) a ON s.payment_id = a.payment_id
			LEFT JOIN (
					SELECT payment_id, SUM(deduction_amount) as deduction_amount
					FROM deduction
					WHERE deduction_name IN ("국민연금", "건강보험", "고용보험")
					GROUP BY payment_id
			) de ON s.payment_id = de.payment_id
			ORDER BY s.payment_id DESC
			""",
			countQuery = "SELECT count(*) FROM salary_payment",
			nativeQuery = true)
	Page<EmployeeSalaryDTO> findAllEmployeeSalary(Pageable pageable);


	@Query(value = """
			SELECT
					s.payment_id as paymentId,
					s.username as username,
					s.payment_date as paymentDate,
					s.base_salary as baseSalary,
			
					e.name as name,
					e.responsibility as responsibility,
			
					d.department_name as departmentName,
					p.position_name as positionName,
			
					IFNULL(a.allowance_amount, 0) as allowanceAmount,
					IFNULL(de.deduction_amount, 0) as deductionAmount
			FROM salary_payment s
			LEFT JOIN employee e ON s.username = e.username
			LEFT JOIN department d ON e.department_id = d.department_id
			LEFT JOIN position p ON e.position_id = p.position_id
			LEFT JOIN (
					SELECT payment_id, SUM(allowance_amount) as allowance_amount
					FROM allowance
					GROUP BY payment_id
			) a ON s.payment_id = a.payment_id
			LEFT JOIN (
					SELECT payment_id, SUM(deduction_amount) as deduction_amount
					FROM deduction
					WHERE deduction_name IN ("국민연금", "건강보험", "고용보험")
					GROUP BY payment_id
			) de ON s.payment_id = de.payment_id
			WHERE s.username = :username
			ORDER BY s.payment_id DESC
			""",
			countQuery = "SELECT count(*) FROM salary_payment WHERE username = :username",
			nativeQuery = true)
	Page<EmployeeSalaryDTO> findAllEmployeeSalaryByUsername(String username, Pageable pageable);

	Optional<SalaryPayment> findByUsernameAndPaymentId(String username, Long paymentId);

//	@Query(
//			"""
//				SELECT s
//				FROM SalaryPayment s
//				WHERE s.username = :username
//				AND FUNCTION('YEAR', s.paymentDate) = FUNCTION('YEAR', :paymentDate)
//				AND FUNCTION('MONTH', s.paymentDate) = FUNCTION('MONTH', :paymentDate)
//			"""
//	)
//	Optional<SalaryPayment> findByIdAndPaymentDate(String username, LocalDateTime paymentDate);
}
