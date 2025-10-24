package com.goodee.corpdesk.salary.entity;

import com.goodee.corpdesk.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity @Table
@DynamicInsert
public class SalaryPayment extends BaseEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentId;
	private String username;
	private LocalDateTime paymentDate;
	private Long baseSalary;
}
