package com.goodee.corpdesk.salary.repository;

import com.goodee.corpdesk.salary.entity.Allowance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AllowanceRepository extends JpaRepository<Allowance, Long> {
	List<Allowance> findAllByPaymentId(Long paymentId);
}
