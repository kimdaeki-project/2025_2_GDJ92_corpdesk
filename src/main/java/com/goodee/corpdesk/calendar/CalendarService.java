package com.goodee.corpdesk.calendar;

import com.goodee.corpdesk.attendance.entity.Attendance;
import com.goodee.corpdesk.attendance.repository.AttendanceRepository;
import com.goodee.corpdesk.schedule.entity.PersonalSchedule;
import com.goodee.corpdesk.schedule.repository.PersonalScheduleRepository;
import com.goodee.corpdesk.vacation.dto.VacationDetailTypeDTO;
import com.goodee.corpdesk.vacation.dto.VacationDetailUsernameDTO;
import com.goodee.corpdesk.vacation.repository.VacationDetailRepository;
import com.goodee.corpdesk.vacation.repository.VacationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {

	private final AttendanceRepository attendanceRepository;
	private final VacationRepository vacationRepository;
	private final VacationDetailRepository vacationDetailRepository;
	private final PersonalScheduleRepository personalScheduleRepository;

	public List<Attendance> getAttendance(CalendarDTO calendarDTO, String username) {
		return attendanceRepository.findAllByUsernameAndDateTime(username, calendarDTO.getStartDateTime(), calendarDTO.getEndDateTime());
	}

	public List<VacationDetailTypeDTO> getVacation(CalendarDTO calendarDTO, String username) {
//		Vacation vacation = vacationRepository.findByUsername(username).get();

		return vacationDetailRepository.findAllByVacationIdAndDate(username, calendarDTO.getStartDate(), calendarDTO.getEndDate());
	}

	public List<PersonalSchedule> getSchedule(CalendarDTO calendarDTO, String username) {
		return personalScheduleRepository.findAllByUsernameAndUseYnTrueAndScheduleDateTimeBetween(username, calendarDTO.getStartDateTime(), calendarDTO.getEndDateTime());
	}

	public List<VacationDetailUsernameDTO> getEveryVacation(CalendarDTO calendarDTO) {
//		Vacation vacation = vacationRepository.findByUsername(username).get();

		return vacationDetailRepository.findEveryByDate(calendarDTO.getStartDate(), calendarDTO.getEndDate());
	}
}
