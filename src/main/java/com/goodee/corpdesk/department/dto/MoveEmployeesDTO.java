package com.goodee.corpdesk.department.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MoveEmployeesDTO {
    private List<String> employeeUsernames;
    private Integer newDeptId;
}
