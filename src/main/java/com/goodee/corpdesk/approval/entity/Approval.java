package com.goodee.corpdesk.approval.entity;

import com.goodee.corpdesk.approval.dto.ReqApprovalDTO;
import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.goodee.corpdesk.approval.dto.ApprovalDTO;
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
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Setter
@Getter
@ToString
@SuperBuilder
@Builder @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "approval")
@DynamicInsert
@DynamicUpdate
public class Approval extends BaseEntity {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long approvalId;
	
	@Column(nullable = false)
	private String username;
	
	@Column(nullable = false)
	private Integer departmentId;

    @Column(nullable = false)
    private Integer approvalFormId;

    @Column(columnDefinition = "longtext")
    private String approvalContent;

	@Column(nullable = false)
	@ColumnDefault("'w'") // 기본값은 w(결재대기)
	private Character status;

    public ApprovalDTO toDTO() {
        return ApprovalDTO.builder()
                        .approvalId(approvalId)
                        .username(username)
                        .departmentId(departmentId)
                        .status(status)
                        .build();
    }

    public ResApprovalDTO toResApprovalDTO() {
        return ResApprovalDTO.builder()
                                .approvalId(approvalId)
                                .username(username)
                                .departmentId(departmentId)
                                .approvalFormId(approvalFormId)
                                .approvalContent(approvalContent)
                                .status(status + "")
                                .createdAt(getCreatedAt())
                                .updatedAt(getUpdatedAt())
                                .build();
    }

    public ReqApprovalDTO toReqApprovalDTO() {
        return ReqApprovalDTO.builder()
                .approvalId(approvalId)
                .username(username)
                .departmentId(departmentId)
                .status(status)
                .build();
    }
	
}
