package com.goodee.corpdesk.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EmployeeDetailDTO {
    private String name;        // 직원 이름 (Employee.employeeName)
    private String positionName;// 직급/직책 이름 (Position.positionName) <- New/Revised
    private String departmentName;// 부서명 (Department.departmentName)
    private String contactEmail;// 연락처 이메일 (Employee.email)
}