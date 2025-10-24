package com.goodee.corpdesk.attendance.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor @AllArgsConstructor
public class ResAttendanceDTO {

    private Long attendanceId;
    private String workStatus;
    private LocalDateTime checkInDateTime;
    private LocalDateTime checkOutDateTime;
    private String username;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private Boolean today;

    private LocalDateTime oldestCheckInDateTime;
 // ✅ 추가
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedAt;
    private Long lateArrivalsCnt;
    private Long absentDaysCnt;
    private Long earlyLeavingsCnt;

    private BigDecimal totalWorkHours;
    private BigDecimal totalWorkDays;

    public ResAttendanceDTO(BigDecimal totalWorkHours, BigDecimal totalWorkDays) {
        this.totalWorkHours = totalWorkHours;
        this.totalWorkDays = totalWorkDays;
    }
    
 // ✅ 화면 표시용 (초 없이 "yyyy-MM-dd HH:mm" 포맷)
    public String getCheckInDateTimeForView() {
        return formatMin(checkInDateTime);
    }

    public String getCheckOutDateTimeForView() {
        return formatMin(checkOutDateTime);
    }

    public String getCreatedAtForView() {
        return formatMin(createdAt);
    }

    public String getUpdatedAtForView() {
        return formatMin(updatedAt);
    }

    private String formatMin(LocalDateTime dt) {
        return dt == null ? "-" : dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    
}
