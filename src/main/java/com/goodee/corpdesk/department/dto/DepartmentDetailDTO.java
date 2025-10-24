package com.goodee.corpdesk.department.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDetailDTO {
    private Integer departmentId;
    private String departmentName;
    private int employeeCount;
    private String createdDate;
    private String parentDepartmentName;
    private List<String> childDepartments;
    private List<MemberDTO> members;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberDTO {
    	private String username;
        private String name;
        private String positionName;
    }
}