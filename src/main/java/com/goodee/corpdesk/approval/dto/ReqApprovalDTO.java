package com.goodee.corpdesk.approval.dto;

import java.util.ArrayList;

import com.goodee.corpdesk.approval.entity.Approval;

import lombok.*;

@Getter
@Setter
@ToString
@Builder @NoArgsConstructor @AllArgsConstructor
public class ReqApprovalDTO {

    // approval
	private Long approvalId;
	private String username;
	private Integer departmentId;
    private Integer approvalFormId;
    private String approvalContent;
	private Character status;
    private Character approveYn;

    // approver
    private Long approverId;
	ArrayList<ApproverDTO> approverDTOList;
	
	public Approval toApprovalEntity() {
		return Approval.builder()
					.approvalId(approvalId)
					.username(username)
					.departmentId(departmentId)
                    .approvalFormId(approvalFormId)
                    .approvalContent(approvalContent)
                    .status(status)
					.build();
	}
	
}
