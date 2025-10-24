package com.goodee.corpdesk.calendar;

import com.goodee.corpdesk.attendance.entity.Attendance;
import com.goodee.corpdesk.schedule.entity.PersonalSchedule;
import com.goodee.corpdesk.vacation.dto.VacationDetailTypeDTO;
import com.goodee.corpdesk.vacation.dto.VacationDetailUsernameDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarApiController {

	private final CalendarService  calendarService;

	@PostMapping("attendance")
	public List<Attendance> getAttendance(@RequestBody CalendarDTO calendarDTO, Authentication authentication) {
//		System.out.println(calendarDTO);
		return calendarService.getAttendance(calendarDTO, authentication.getName());
	}

	@PostMapping("vacation")
	public List<VacationDetailTypeDTO> getVacation(@RequestBody CalendarDTO calendarDTO, Authentication authentication) {
		return calendarService.getVacation(calendarDTO, authentication.getName());
	}

	@PostMapping("schedule")
	public List<PersonalSchedule> getSchedule(@RequestBody CalendarDTO calendarDTO, Authentication authentication) {
		return calendarService.getSchedule(calendarDTO, authentication.getName());
	}

	@PostMapping("everyVacation")
	public List<VacationDetailUsernameDTO> getEveryVacation(@RequestBody CalendarDTO calendarDTO) {
		return calendarService.getEveryVacation(calendarDTO);
	}
}
