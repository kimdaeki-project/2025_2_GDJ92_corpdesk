package com.goodee.corpdesk.approval.entity;

import com.goodee.corpdesk.approval.dto.ApproverDTO;
import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.goodee.corpdesk.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity @Table(name = "approver")
@DynamicInsert
@DynamicUpdate
public class Approver extends BaseEntity {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long approverId;
	
	@Column(nullable = false)
	private Long approvalId;
	
	@Column(nullable = false)
	private String username;
	
	@Column(nullable = false)
	private Integer approvalOrder;
	
	@Column(nullable = false)
	@ColumnDefault("'w'") // 기본값은 w(결재대기)
	private Character approveYn;

    public ApproverDTO toDTO() {
        return ApproverDTO.builder()
                          .approverId(approverId)
                          .approvalId(approvalId)
                          .username(username)
                          .approvalOrder(approvalOrder)
                          .approveYn(approveYn)
                          .build();
    }

    public ResApprovalDTO toResApprovalDTO() {
        return ResApprovalDTO.builder()
                .approverId(approverId)
                .build();
    }
}
