package com.goodee.corpdesk.employee;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeInfoDTO {

	private String username;
	private String name;
	private LocalDate hireDate;

	private String departmentName;

	private String positionName;
}
