package com.goodee.corpdesk.vacation.entity;

import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import com.goodee.corpdesk.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Setter
@Getter
@ToString
@Entity @Table(name = "vacation_type")
@Builder
@NoArgsConstructor @AllArgsConstructor
@DynamicInsert @DynamicUpdate
public class VacationType extends BaseEntity {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // TODO 서버실행시 기본값 넣기위해 임시로 주석처리
    private Integer vacationTypeId;

    private String vacationTypeName;


    public ResApprovalDTO toResApprovalDTO() {
        return ResApprovalDTO.builder()
                .vacationTypeId(vacationTypeId)
                .vacationTypeName(vacationTypeName)
                .build();
    }

}
