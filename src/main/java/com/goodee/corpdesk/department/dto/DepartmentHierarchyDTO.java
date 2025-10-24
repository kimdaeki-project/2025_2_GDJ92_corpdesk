package com.goodee.corpdesk.department.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentHierarchyDTO {
    private Integer departmentId;
    private String departmentName;
    private Integer parentDepartmentId;
    private Integer employeeCount; // 부서 인원 수
    private List<DepartmentHierarchyDTO> children = new ArrayList<>();
}
