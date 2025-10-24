package com.goodee.corpdesk.vacation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VacationDetailUsernameDTO {

	private Long vacationDetailId;
	private Integer vacationId;
	private Integer vacationTypeId;
	private LocalDate startDate;
	private LocalDate endDate;

	private String username;
}
