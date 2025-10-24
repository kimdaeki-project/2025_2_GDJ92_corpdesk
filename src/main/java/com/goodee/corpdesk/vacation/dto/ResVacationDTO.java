package com.goodee.corpdesk.vacation.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor @AllArgsConstructor
public class ResVacationDTO {

    // vacation
    private Integer vacationId;
    private Integer totalVacation;
    private Integer remainingVacation;
    private Integer usedVacation;

    // vacation_detail
    private Long vacationDetailId;
    private Integer vacationTypeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer usedDays;

    private String name;
    private String department;
    private String position;

}
