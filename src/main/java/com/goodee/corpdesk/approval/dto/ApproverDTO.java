package com.goodee.corpdesk.approval.dto;

import com.goodee.corpdesk.approval.entity.Approver;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApproverDTO {
	
	private Long approverId;
	private Long approvalId;
	private String username;
	private Integer approvalOrder;
	private Character approveYn;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // employee
    private String name;

    // department
    private String departmentName;

    // position
    private String positionName;
	
	public Approver toEntity() {
		return Approver.builder()
					.approverId(approverId)
					.approvalId(approvalId)
					.username(username)
					.approvalOrder(approvalOrder)
					.approveYn(approveYn)
					.build();
	}

    // Hibernate는 리플렉션을 사용해서 DTO를 생성할 때 컬럼명이 아닌 **위치(순서)**로 생성자 매개변수와 매핑함. 따라서 SELECT 컬럼 순서와 생성자 매개변수 순서가 정확히 일치해야 함.
    public ApproverDTO(Long approverId, Long approvalId, String username
            , Integer approvalOrder, Character approveYn, Timestamp createdAt
            , String name, String departmentName, String positionName) {
        this.approverId = approverId;
        this.approvalId = approvalId;
        this.username = username;
        this.approvalOrder = approvalOrder;
        this.approveYn = approveYn;
        this.createdAt = LocalDateTime.ofInstant(createdAt.toInstant(), ZoneId.of("Asia/Seoul"));
        this.name = name;
        this.departmentName = departmentName;
        this.positionName = positionName;
    }
}
