package com.goodee.corpdesk.salary.repository;

import com.goodee.corpdesk.salary.entity.Deduction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeductionRepository extends JpaRepository<Deduction, Long> {
	List<Deduction> findAllByPaymentId(Long paymentId);
}
