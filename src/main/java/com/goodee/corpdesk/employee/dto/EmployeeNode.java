package com.goodee.corpdesk.employee.dto;

import lombok.Data;

@Data
public class EmployeeNode {
    private String employeeName;
    private String positionName;
    private String departmentName;

    // 화면 출력용 조합 문자열
    public String getDisplayName() {
        return employeeName + " " + positionName;
    }

    public String getSubInfo() {
        return departmentName + " > " + positionName;
    }
}
