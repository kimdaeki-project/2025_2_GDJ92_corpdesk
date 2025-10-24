package com.goodee.corpdesk.approval.repository;

import com.goodee.corpdesk.approval.entity.ApprovalForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalFormRepository extends JpaRepository<ApprovalForm, Integer> {

}
