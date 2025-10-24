package com.goodee.corpdesk.salary.entity;

import com.goodee.corpdesk.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table
@DynamicInsert
public class Allowance extends BaseEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long allowanceId;
	private Long paymentId;
	private String allowanceName;
	private Long allowanceAmount;
}
