package com.goodee.corpdesk.approval.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ApprovalFileDTO {

    private Long fileId;
    private Long approvalId;
    private String oriName;
    private String saveName;
    private String extension;

}