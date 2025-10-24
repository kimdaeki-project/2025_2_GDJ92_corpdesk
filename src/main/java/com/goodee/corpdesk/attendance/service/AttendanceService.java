package com.goodee.corpdesk.attendance.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.goodee.corpdesk.attendance.DTO.ResAttendanceDTO;
import com.goodee.corpdesk.attendance.entity.Attendance;
import com.goodee.corpdesk.attendance.repository.AttendanceRepository;
import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeRepository;
import com.goodee.corpdesk.vacation.entity.VacationDetail;
import com.goodee.corpdesk.vacation.repository.VacationDetailRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final VacationDetailRepository vacationDetailRepository;
    private final EmployeeRepository employeeRepository;

    @Value("${attendance.work-hour.start}")
    private String workStartHour;
    @Value("${attendance.work-hour.end}")
    private String workEndHour;

    /**
     * 새 출퇴근 내역을 생성하거나 기존 출퇴근 내역을 수정합니다.
     *
     * 현재 인증된 사용자로 'modifiedBy'를 설정합니다.
     * insert하는 경우 'createdAt'을 설정합니다.
     * update하는 경우 'updatedAt'을 설정합니다.
     *
     * @param attendance 생성 혹은 수정할 Attendance entity
     * @return 생성 혹은 수정된 Attendance entity
     */
    public Attendance saveOrUpdateAttendance(Attendance attendance) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        attendance.setModifiedBy(currentUser);

        // 초/나노 버림
        attendance.setCheckInDateTime(trimToMinutes(attendance.getCheckInDateTime()));
        attendance.setCheckOutDateTime(trimToMinutes(attendance.getCheckOutDateTime()));

        // 신규 INSERT 인지
        boolean isInsert = (attendance.getAttendanceId() == null);

        if (isInsert) {
            // === [신규] 구분값 기준으로 분기 ===
            String ws = attendance.getWorkStatus();

            if ("출근".equals(ws)) {
                // 열린 근무가 이미 있으면 막기
                attendanceRepository
                    .findTopByUsernameAndUseYnTrueAndCheckInDateTimeIsNotNullAndCheckOutDateTimeIsNullOrderByCheckInDateTimeDesc(attendance.getUsername())
                    .ifPresent(a -> { throw new IllegalStateException("이미 출근 상태입니다. (퇴근 미처리)"); });

                if (attendance.getCheckInDateTime() == null) {
                    attendance.setCheckInDateTime(trimToMinutes(LocalDateTime.now()));
                }
                attendance.setCreatedAt(trimToMinutes(LocalDateTime.now()));
                attendance.setUpdatedAt(attendance.getCreatedAt());
                return attendanceRepository.save(attendance);
            }

            if ("퇴근".equals(ws)) {
                // ✅ 신규행을 만들지 말고 열린 근무를 찾아서 퇴근시간만 채움
                Attendance open = attendanceRepository
                    .findTopByUsernameAndUseYnTrueAndCheckInDateTimeIsNotNullAndCheckOutDateTimeIsNullOrderByCheckInDateTimeDesc(attendance.getUsername())
                    .orElseThrow(() -> new IllegalStateException("출근 기록이 없습니다. (열린 근무 없음)"));

                LocalDateTime outTime = attendance.getCheckOutDateTime() != null
                        ? attendance.getCheckOutDateTime()
                        : trimToMinutes(LocalDateTime.now());

                if (outTime.isBefore(open.getCheckInDateTime())) {
                    throw new IllegalArgumentException("퇴근 시각이 출근 시각보다 이릅니다.");
                }

                open.setCheckOutDateTime(outTime);
                open.setWorkStatus("퇴근"); // workStatus를 계속 저장한다면 유지
                open.setModifiedBy(currentUser);
                open.setUpdatedAt(trimToMinutes(LocalDateTime.now()));
                return attendanceRepository.save(open);
            }

            // 그 외 상태(예: 결근) 신규저장 허용 시
            attendance.setCreatedAt(trimToMinutes(LocalDateTime.now()));
            attendance.setUpdatedAt(attendance.getCreatedAt());
            return attendanceRepository.save(attendance);

        } else {
            // === [업데이트] ===
            // checkIn / checkOut 수동 수정 등
            if (attendance.getCheckInDateTime() != null && attendance.getCheckOutDateTime() != null
                    && attendance.getCheckOutDateTime().isBefore(attendance.getCheckInDateTime())) {
                throw new IllegalArgumentException("퇴근 시각이 출근 시각보다 이릅니다.");
            }

            attendance.setUpdatedAt(trimToMinutes(LocalDateTime.now()));
            return attendanceRepository.save(attendance);
        }
    }


    // 헬퍼
    private static LocalDateTime trimToMinutes(LocalDateTime dt) {
        return dt == null ? null : dt.withSecond(0).withNano(0);
    }

    /**
     * 출퇴근pk로 출퇴근 내역을 조회합니다.
     *
     * @param id 출퇴근pk
     * @return id로 조회한 Attendance entity
     * @throws RuntimeException 만약 id로 조회할 출퇴근 내역이 없다면 발생합니다.
     */
    public Attendance getAttendanceById(Long id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance record not found"));
    }

    /**
     * 제공된 Attendance 엔티티에 대한 변경 사항을 영속화합니다.
     *
     * 주어진 Attendance 엔티티를 레포지토리에 저장하며, 이미 존재하는 엔티티일 경우 업데이트를 적용합니다.
     *
     * @param attendance 저장할 Attendance 엔티티
     */
    public void updateAttendance(Attendance attendance) {
        attendanceRepository.save(attendance);
    }

    /**
     * 지정된 출퇴근 기록을 비활성(inactive)으로 표시하여 소프트 삭제합니다.
     *
     * 식별된 각 기록은 `useYn`이 `false`로 설정되고, `modifiedBy`는 현재 인증된 사용자로,
     * `updatedAt`은 현재 시간으로 설정됩니다.
     *
     * @param username      삭제를 요청하는 사용자 이름 (기록 필터링에는 사용되지 않습니다.)
     * @param attendanceIds 비활성으로 표시할 출퇴근 기록 ID 목록
     */
    public void deleteAttendances(String username, java.util.List<Long> attendanceIds) {
        attendanceIds.forEach(id -> {
            Attendance att = getAttendanceById(id);
            att.setUseYn(false);
            att.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            att.setUpdatedAt(LocalDateTime.now());
            attendanceRepository.save(att);
        });
    }

    /**
     * 지정된 직원의 활성(Active) 출퇴근 기록을 조회합니다.
     *
     * @param username 직원의 사용자 이름
     * @return 주어진 사용자 이름에 대해 `useYn`이 `true`인 Attendance 엔티티 목록
     */
    public List<Attendance> getAttendanceByUsername(String username) {
        return attendanceRepository.findByUsernameAndUseYn(username, true);
    }


    // =================== passing7by

    // 특정 직원의 현재 상태(휴가/퇴근/출근/출근전) & 출퇴근id & 출퇴근일시 조회
    public ResAttendanceDTO getCurrentAttendance(String username) throws Exception {
        ResAttendanceDTO dto = new ResAttendanceDTO();

        // 휴일/휴가 로직은 유지
        LocalDate today = LocalDate.now();
        boolean isWeekend = today.getDayOfWeek().getValue() >= 6;
        VacationDetail vacationDetail = vacationDetailRepository.findVacationDetailOnDate(username, today);
        if (!isWeekend && vacationDetail != null) {
            dto.setWorkStatus("휴가");
            return dto;
        }

        Attendance latest = attendanceRepository.findLatestAttendanceByUsername(username);
        if (latest == null) {
            dto.setWorkStatus("출근전");
            return dto;
        }

     // 상태 파생 (null-safe + 이전일 열린 세션 처리)
        String status;
        LocalDateTime in  = latest.getCheckInDateTime();
        LocalDateTime out = latest.getCheckOutDateTime();

        if (in == null) {
            status = "출근전";
        } else if (out == null) {
            // 열린 근무(퇴근 미처리)
            if (in.toLocalDate().isEqual(today)) {
                status = "출근";           // 오늘 출근했고 아직 퇴근 전
            } else {
                status = "퇴근미처리";     // 어제(또는 과거) 출근만 있고 퇴근 누락
            }
        } else {
            // 출퇴근 모두 채워짐
            status = in.toLocalDate().isEqual(today) ? "퇴근" : "출근전";
        }

        dto.setWorkStatus(status);
        dto.setAttendanceId(latest.getAttendanceId());
        dto.setCheckInDateTime(latest.getCheckInDateTime());
        dto.setCheckOutDateTime(latest.getCheckOutDateTime());
        dto.setCheckInDate(latest.getCheckInDateTime() == null ? null : latest.getCheckInDateTime().toLocalDate());
        dto.setCheckOutDate(latest.getCheckOutDateTime() == null ? null : latest.getCheckOutDateTime().toLocalDate());
        dto.setCheckInTime(latest.getCheckInDateTime() == null ? null : latest.getCheckInDateTime().toLocalTime());
        dto.setCheckOutTime(latest.getCheckOutDateTime() == null ? null : latest.getCheckOutDateTime().toLocalTime());
        dto.setToday(latest.getCheckInDateTime() != null && latest.getCheckInDateTime().toLocalDate().isEqual(today));
        return dto;
    }

    public List<Integer> getYearRangeByEmployee(String username) throws Exception {
        Integer oldestYear = attendanceRepository.findOldestCheckInYearByUsername(username);
        int currentYear = LocalDate.now().getYear();

        if (oldestYear == null) {
            return List.of(currentYear);
        }

        List<Integer> years = new ArrayList<>();
        for (int year = oldestYear; year <= currentYear; year++) {
            years.add(year);
        }
        return years;
    }

    // 특정 직원의 지각, 조퇴, 결근 횟수를 한 번에 묶어서 반환하는 DTO를 생성합니다.
    // year와 month가 null이면 전체 횟수를 조회합니다.
    public ResAttendanceDTO getAttendanceCounts(String username, String year, String month) throws Exception {

        ResAttendanceDTO  resAttendanceDTO = new ResAttendanceDTO();
        LocalTime workStartTime = LocalTime.parse(workStartHour);
        LocalTime workEndTime = LocalTime.parse(workEndHour);

        resAttendanceDTO.setLateArrivalsCnt(
                attendanceRepository.countLateArrivalsByUsernameAndYearMonth(workStartTime, username,  year, month)
        );
        resAttendanceDTO.setAbsentDaysCnt(
                attendanceRepository.countAbsentDaysByUsernameAndYearMonth(username, year, month)
        );
        resAttendanceDTO.setEarlyLeavingsCnt(
                attendanceRepository.countEarlyLeavingsByUsernameAndYearMonth(workEndTime, username, year, month)
        );

        return resAttendanceDTO;

    }

    // 특정 직원의 총 근무 시간 및 총 근무 일수를 조회하여 반환합니다.
    // year와 month가 null이면 전체 횟수를 조회합니다.
    public ResAttendanceDTO getWorkSummary(String username, String year, String month) throws Exception {

       return attendanceRepository.findWorkSummaryByUsernameAndYearMonth(username, year, month, LocalDateTime.now());

    }

    // 특정 직원의 상세 출퇴근 기록 목록 (출근일, 출퇴근 시간, 근무 상태 등)을 조회합니다.
    // year와 month가 null이면 전체 횟수를 조회합니다.
    public List<ResAttendanceDTO> getAttendanceDetailList(String username, String year, String month) throws Exception {
        return attendanceRepository.findByUseYnAndUsernameAndYearMonth(username, year, month).stream().map(Attendance::toDTO).toList();
    }

    public ResAttendanceDTO checkIn(String username) {
        // 열린 근무가 있으면 막기
        attendanceRepository
            .findTopByUsernameAndUseYnTrueAndCheckInDateTimeIsNotNullAndCheckOutDateTimeIsNullOrderByCheckInDateTimeDesc(username)
            .ifPresent(a -> { throw new IllegalStateException("이미 출근 상태입니다. (퇴근 미처리)"); });

        Attendance a = new Attendance();
        a.setUsername(username);
        a.setCheckInDateTime(trimToMinutes(LocalDateTime.now()));
        a.setWorkStatus("출근");
        a.setModifiedBy(username);
        a.setCreatedAt(a.getCheckInDateTime());
        a.setUpdatedAt(a.getCheckInDateTime());

        return attendanceRepository.save(a).toDTO();
    }

    public ResAttendanceDTO checkOut(Long attendanceId, String username) {
        // attendanceId로 특정 근무를 종료하거나,
        // 안전하게 열린 근무를 우선 찾도록 처리(둘 중 하나 선택)
        Attendance target;
        if (attendanceId != null) {
            target = attendanceRepository.findById(attendanceId)
                     .orElseThrow(() -> new IllegalArgumentException("근무 기록이 없습니다."));
        } else {
            target = attendanceRepository
                     .findTopByUsernameAndUseYnTrueAndCheckInDateTimeIsNotNullAndCheckOutDateTimeIsNullOrderByCheckInDateTimeDesc(username)
                     .orElseThrow(() -> new IllegalStateException("출근 기록이 없습니다. (열린 근무 없음)"));
        }

        LocalDateTime now = trimToMinutes(LocalDateTime.now());
        if (target.getCheckInDateTime() != null && now.isBefore(target.getCheckInDateTime())) {
            throw new IllegalArgumentException("퇴근 시각이 출근 시각보다 이릅니다.");
        }

        target.setCheckOutDateTime(now);
        target.setWorkStatus("퇴근");
        target.setModifiedBy(username);
        target.setUpdatedAt(now);

        return attendanceRepository.save(target).toDTO();
    }

    public void recordAbsence() {
        LocalDate target = LocalDate.now().minusDays(1); // 어제

        List<Employee> employees = employeeRepository.findAllByUseYnTrue();
        for (Employee e : employees) {
            // 입사 전은 건너뛰기
            if (e.getHireDate() != null && target.isBefore(e.getHireDate())) continue;

            // 어제 휴가면 건너뛰기
            VacationDetail vd = vacationDetailRepository.findVacationDetailOnDate(e.getUsername(), target);
            if (vd != null) continue;

            // 어제 날짜에 출근 기록이 있었는지 검사
            Attendance latest = attendanceRepository.findLatestAttendanceByUsername(e.getUsername());
            boolean workedYesterday = false;
            if (latest != null) {
                if (latest.getCheckInDateTime() != null
                        && latest.getCheckInDateTime().toLocalDate().isEqual(target)) {
                    workedYesterday = true;
                }
                // 야간근무가 자정을 넘는다면, checkOutDateTime의 날짜도 고려
                if (!workedYesterday && latest.getCheckOutDateTime() != null
                        && latest.getCheckOutDateTime().toLocalDate().isEqual(target)) {
                    workedYesterday = true;
                }
            }
            if (workedYesterday) continue;

            // 결근 레코드 생성 (workStatus만 기록하는 정책 유지 시)
            Attendance absence = new Attendance();
            absence.setUsername(e.getUsername());
            absence.setWorkStatus("결근");
            absence.setCreatedAt(trimToMinutes(LocalDateTime.now()));
            absence.setUpdatedAt(absence.getCreatedAt());
            attendanceRepository.save(absence);
        }
    }


}

