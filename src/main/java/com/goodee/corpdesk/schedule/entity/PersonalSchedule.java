package com.goodee.corpdesk.schedule.entity;

import com.goodee.corpdesk.common.BaseEntity;
import com.goodee.corpdesk.schedule.dto.ResPersonalScheduleDTO;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@SuperBuilder
@Builder
@NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "personal_schedule")
@DynamicInsert @DynamicUpdate
public class PersonalSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personalScheduleId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String scheduleName;

    @Column(nullable = false)
    private LocalDateTime scheduleDateTime;

    @Column(columnDefinition = "text")
    private String content;

    private String address;

    public ResPersonalScheduleDTO toResPersonalScheduleDTO() {
        return ResPersonalScheduleDTO.builder()
            .personalScheduleId(personalScheduleId)
            .scheduleName(scheduleName)
            .scheduleDateTime(scheduleDateTime)
            .content(content)
            .address(address)
            .build();
    }

}
