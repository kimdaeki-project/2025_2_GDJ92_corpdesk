package com.goodee.corpdesk.attendance.entity;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.goodee.corpdesk.attendance.DTO.ResAttendanceDTO;
import com.goodee.corpdesk.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "attendance")
@DynamicInsert
@DynamicUpdate
public class Attendance extends BaseEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @Column(nullable = false)
    private String username;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime checkInDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime checkOutDateTime;

    private Character isHoliday;

    @Column(nullable = false)
    private String workStatus;

    // ====== 기존 표시/입력용 ======
    public String getFormattedCheckInDateTime() {
        if (checkInDateTime == null) return "";
        return checkInDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String getFormattedCheckOutDateTime() {
        if (checkOutDateTime == null) return "";
        return checkOutDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String getCheckInDateTimeForInput() {
        return checkInDateTime != null
            ? checkInDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
            : "";
    }

    public String getCheckOutDateTimeForInput() {
        return checkOutDateTime != null
            ? checkOutDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
            : "";
    }

    // ====== ⬇⬇ 추가: 생성/수정일시 '분까지' 표시용 게터 ======
    @Transient
    public String getCreatedAtForView() {
        return fmtMin(getCreatedAt()); // BaseEntity의 createdAt 사용
    }

    @Transient
    public String getUpdatedAtForView() {
        return fmtMin(getUpdatedAt()); // BaseEntity의 updatedAt 사용
    }

    // 필요하면 체크인/아웃도 같은 포맷으로 보여줄 때 사용 가능
    @Transient
    public String getCheckInDateTimeForView() {
        return fmtMin(checkInDateTime);
    }

    @Transient
    public String getCheckOutDateTimeForView() {
        return fmtMin(checkOutDateTime);
    }

    private String fmtMin(LocalDateTime dt) {
        return dt == null ? "-" : dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    

    public ResAttendanceDTO toDTO() {
        return ResAttendanceDTO.builder()
                                .attendanceId(attendanceId)
                                .username(username)
                                .checkInDateTime(checkInDateTime)
                                .checkOutDateTime(checkOutDateTime)
                                .workStatus(workStatus)
                                .build();
    }

}
