package com.goodee.corpdesk.file.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

@Setter
@Getter
@ToString(callSuper = true)
@Entity @Table(name = "employee_file")
@DynamicInsert
public class EmployeeFile extends FileBase {
	@Column(nullable = false)
	private String username;
}
