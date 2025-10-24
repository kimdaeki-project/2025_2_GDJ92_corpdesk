package com.goodee.corpdesk.schedule.dto;

import com.goodee.corpdesk.schedule.entity.PersonalSchedule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ReqPersonalScheduleDTO {

    private String username;
    private String scheduleName;
    private LocalDateTime scheduleDateTime;
    private String address;
    private String content;

    private Integer year;
    private Integer month;

    public PersonalSchedule toEntity() {
        return PersonalSchedule.builder()
            .username(username)
            .scheduleName(scheduleName)
            .scheduleDateTime(scheduleDateTime)
            .address(address)
            .content(content)
            .build();
    }

}
