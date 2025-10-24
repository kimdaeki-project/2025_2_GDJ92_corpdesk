package com.goodee.corpdesk.attendance;

import com.goodee.corpdesk.attendance.DTO.ResAttendanceDTO;
import com.goodee.corpdesk.attendance.service.AttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class AttendanceServiceTest {
    @Autowired
    AttendanceService attendanceService;

    @Test
    void attendanceStatus() {
//        ResAttendanceDTO resAttendanceDTO = attendanceService.getAttendanceStatus("choi_cto");
//        log.warn("{}", resAttendanceDTO);
//
//        assertNotNull(resAttendanceDTO);
    }
}