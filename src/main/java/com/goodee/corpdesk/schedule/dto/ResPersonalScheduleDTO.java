package com.goodee.corpdesk.schedule.dto;

import com.goodee.corpdesk.schedule.entity.PersonalSchedule;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResPersonalScheduleDTO {

    private Long personalScheduleId;
    private String scheduleName;
    private LocalDateTime scheduleDateTime;
    private String content;
    private String address;

    private List<ResPersonalScheduleDTO> schedules;
    private Integer todayScheduleCnt;
    private Integer totalScheduleCnt;

    private Double latitude; // 위도(y)
    private Double longitude; // 경도(x)

    public ResPersonalScheduleDTO(Long personalScheduleId, String scheduleName
        , LocalDateTime scheduleDateTime, String content, String address) {
        this.personalScheduleId = personalScheduleId;
        this.scheduleName = scheduleName;
        this.scheduleDateTime = scheduleDateTime;
        this.content = content;
        this.address = address;
    }

}
