package com.goodee.corpdesk.employee;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResEmployeeDTO {

    // Employee
    private String username;
    private Integer positionId;
    private Integer departmentId;
    private String name;

    // Department
    private String departmentName;
    private Integer parentDepartmentId;

    // Position
    private String positionName;
    private Integer parentPositionId;

    // File
    private String saveName;
    private String extension;

    public ResEmployeeDTO(String username, Integer positionId, Integer departmentId
        , String name, String departmentName, Integer parentDepartmentId
        , String positionName, Integer parentPositionId) {
        this.username = username;
        this.positionId = positionId;
        this.departmentId = departmentId;
        this.name = name;
        this.departmentName = departmentName;
        this.parentDepartmentId = parentDepartmentId;
        this.positionName = positionName;
        this.parentPositionId = parentPositionId;
    }
}
