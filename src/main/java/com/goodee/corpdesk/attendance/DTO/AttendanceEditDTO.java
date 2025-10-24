package com.goodee.corpdesk.attendance.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceEditDTO {
    private Long attendanceId;
    private String workStatus;
    private String dateTime;
    private String checkInDateTime;  // yyyy-MM-dd'T'HH:mm
    private String checkOutDateTime; // yyyy-MM-dd'T'HH:mm
    
}