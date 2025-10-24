package com.goodee.corpdesk.approval.repository;

import java.util.List;

import com.goodee.corpdesk.approval.dto.ApproverDTO;
import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import com.goodee.corpdesk.approval.entity.Approver;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;

public interface ApproverRepository extends JpaRepository<Approver, Long> {
	
	List<Approver> findAllByApprovalId(Long approvalId);

    @NativeQuery("""
        WITH a2 AS (
        	SELECT *
        	FROM approver
        	WHERE approval_id = :approvalId
        )
        SELECT 
            a2.approver_id AS approverId, a2.approval_id AS approvalId, a2.username AS username
            , a2.approval_order AS approvalOrder, a2.approve_yn AS approveYn, a2.created_at AS createdAt
            , e.name AS name, d.department_name AS departmentName, p.position_name AS positionName
        FROM a2
        JOIN employee e USING (username)
        JOIN department d USING (department_id)
        JOIN `position` p USING (position_id)
    """)
    List<ApproverDTO> findByApprovalIdWithEmployeeAndDepartment(Long approvalId);

	Approver findByApprovalIdAndApproverId(Long approvalId, Long approverId);
	Approver findByApprovalIdAndApprovalOrder(Long approvalId, Integer approvalOrder);

    Approver findAllByApprovalIdAndUsername(Long approvalId, String username);

}
