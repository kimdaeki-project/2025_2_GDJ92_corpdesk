package com.goodee.corpdesk.holiday.dto;

import com.goodee.corpdesk.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class HolidayDTO {

    private String dateName;
    private LocalDate locdate;
    private Character isHoliday;

}
