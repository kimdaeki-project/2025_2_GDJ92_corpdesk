package com.goodee.corpdesk.approval.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.goodee.corpdesk.approval.entity.Approval;
import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.vacation.entity.VacationType;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResApprovalDTO {

    // approval
	private Long approvalId;
	private String username;
    private String name;
	private Integer departmentId;
	private Integer positionId;
    private Integer approvalFormId;
    private String approvalContent;
	private String status;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

    // approver
    private Long approverId;
	private List<ApproverDTO> approverDTOList;

    // approval_form
    private String formTitle;
    private String formContent;

    // department
    private String departmentName;
    private Integer parentDepartmentId;

    // position
    private String positionName;

    // file
    private Integer fileCount;
    private List<ApprovalFileDTO> files;

    private Long fileId;
    private String oriName;
    private String saveName;
    private String extension;

    // vacation
    private Integer vacationTypeId;
    private String vacationTypeName;

    private List<ResApprovalDTO> approvals;
    private Integer approvalCnt;

    private boolean processed; // 결재 진행중 혹은 진행완료 = true, 그 외 = false

    // SQL 결과용 생성자
    public ResApprovalDTO(Long approvalId, LocalDateTime createdAt, Character status, String username,
                          String formTitle, Long fileCount, String departmentName) {
        this.approvalId = approvalId;
        this.createdAt = createdAt;
        this.status = status + "";
        this.username = username;
        this.formTitle = formTitle;
        this.fileCount = fileCount != null ? fileCount.intValue() : 0;
        this.departmentName = departmentName;
    }

    public ResApprovalDTO(Long approvalId, LocalDateTime createdAt, Character status,
                          String formTitle, Long fileCount, String departmentName) {
        this.approvalId = approvalId;
        this.createdAt = createdAt;
        this.status = status + "";
        this.formTitle = formTitle;
        this.fileCount = fileCount != null ? fileCount.intValue() : 0;
        this.departmentName = departmentName;
    }

    public ResApprovalDTO(String departmentName, String username, String name, String positionName,
                          Long fileId, String oriName, String saveName, String extension) {
        this.departmentName = departmentName;
        this.username = username;
        this.name = name;
        this.positionName = positionName;
        this.fileId = fileId;
        this.oriName = oriName;
        this.saveName = saveName;
        this.extension = extension;
    }
}
