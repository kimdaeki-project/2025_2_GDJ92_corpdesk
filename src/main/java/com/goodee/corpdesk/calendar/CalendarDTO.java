package com.goodee.corpdesk.calendar;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class CalendarDTO {

	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;

	public LocalDate getStartDate() {
		return this.startDateTime.toLocalDate();
	}

	public LocalDate getEndDate() {
		return this.endDateTime.toLocalDate();
	}
}
