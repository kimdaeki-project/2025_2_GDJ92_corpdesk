package com.goodee.corpdesk.vacation.entity;

import com.goodee.corpdesk.common.BaseEntity;
import com.goodee.corpdesk.vacation.dto.ResVacationDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@Entity @Table(name = "vacation_detail")
@DynamicInsert @DynamicUpdate
public class VacationDetail extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vacationDetailId;

    @Column(nullable = false)
    private Integer vacationId;

    @Column(nullable = false)
    private Integer vacationTypeId;

    @Column(nullable = false)
    private Long approvalId;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    private Integer usedDays;

    public ResVacationDTO toResVacationDTO() {
        return ResVacationDTO.builder()
            .vacationId(vacationId)
            .vacationDetailId(vacationDetailId)
            .vacationTypeId(vacationTypeId)
            .startDate(startDate)
            .endDate(endDate)
            .usedDays(usedDays)
            .build();
    }

}
