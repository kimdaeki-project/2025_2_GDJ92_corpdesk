package com.goodee.corpdesk.salary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployeeSalaryDTO {

	private Long paymentId;
	private String username;
	private Timestamp paymentDate;
	private Long baseSalary;

	private String name;
	private String responsibility;

	private String departmentName;

	private String positionName;

	private BigDecimal allowanceAmount;
	private BigDecimal deductionAmount;

//	private Timestamp updatedAt;
//	private Timestamp createdAt;
//	private String modifiedBy;
//	private Boolean useYn;
}
