document.addEventListener('DOMContentLoaded', function () {

	const checkAttendance = document.getElementById('customCheckPrimary');
	const checkVacation = document.getElementById('customCheckSecondary');
	const checkSchedule = document.getElementById('customCheckSuccess');
	const checkEveryVacation = document.getElementById('customCheckWarning');

	let currentDateInfo = null;

	const calendarEl = document.getElementById('calendar');
	const calendar = new FullCalendar.Calendar(calendarEl, {
		initialView: 'dayGridMonth',
		headerToolbar: {
			left: 'dayGridMonth dayGridWeek listDay',
			center: 'title',
			right: 'today prev,next',
		},
		views: {
			// dayGridMonth: {
			// 	year: 'numeric',
			// 	month: 'long',
			// },
			// listWeek: {
			// 	noEventsText: '이번 주 일정이 없습니다.',
			// 	buttonText: 'week'
			// },
			listDay: {
				noEventsContent: '오늘 일정이 없습니다.',
				buttonText: 'day'
			}
		},
		datesSet: function (info) {
			calendar.removeAllEvents();
			currentDateInfo = info;
			if (checkAttendance.checked == true) getAttendance(info);
			if (checkVacation.checked == true) getVacation(info);
			if (checkSchedule.checked == true) getSchedule(info);
			if (checkEveryVacation.checked == true) getEveryVacation(info);
		},
		themeSystem: 'bootstrap5',
	});
	calendar.render();

	checkAttendance.addEventListener('change', function () {
		if (checkAttendance.checked == true) getAttendance(currentDateInfo);
		else {
			calendar.getEvents().forEach(event => {
				if (event.groupId == 'attendance') {
					event.remove();
				}
			})
		}
	})

	async function getAttendance(info) {
		try {
			const response = await fetch('/api/calendar/attendance', {
				method: 'POST',
				headers: {
					'content-type': 'application/json',
				},
				body: JSON.stringify({
					startDateTime: info.start,
					endDateTime: info.end
				})
			});

			if (!response.ok) throw new Error('수신 오류');
			const data = await response.json();

			data.forEach(a => {
				calendar.addEvent({
					id: 'attendance' + a.attendanceId,
					title: a.workStatus,
					start: a.checkInDateTime,
					end: a.checkOutDateTime,
					groupId: 'attendance',
					backgroundColor: '#9e6de0'
				});
			});
		} catch (error) {
			console.error(error)
		}
	}

	checkVacation.addEventListener('change', function () {
		if (checkVacation.checked == true) getVacation(currentDateInfo);
		else {
			calendar.getEvents().forEach(event => {
				if (event.groupId == 'vacation') {
					event.remove();
				}
			})
		}
	})

	async function getVacation(info) {
		try {
			const response = await fetch('/api/calendar/vacation', {
				method: 'POST',
				headers: {
					'content-type': 'application/json',
				},
				body: JSON.stringify({
					startDateTime: info.start,
					endDateTime: info.end
				})
			});

			if (!response.ok) throw new Error('수신 오류');
			const data = await response.json();

			data.forEach(v => {
				let endDate = new Date(v.endDate);
				endDate.setDate(endDate.getDate() + 1);
				calendar.addEvent({
					id: 'vacation' + v.vacationId,
					title: v.vacationTypeName, // FIXME: 휴가 종류
					start: v.startDate,
					end: endDate,
					allDay: true,
					groupId: 'vacation',
					backgroundColor: '#fd5190'
				});
			});
		} catch (error) {
			console.error(error)
		}
	}

	checkEveryVacation.addEventListener('change', function () {
		if (checkEveryVacation.checked == true) getEveryVacation(currentDateInfo);
		else {
			calendar.getEvents().forEach(event => {
				if (event.groupId == 'everyVacation') {
					event.remove();
				}
			})
		}
	})

	async function getEveryVacation(info) {
		try {
			const response = await fetch('/api/calendar/everyVacation', {
				method: 'POST',
				headers: {
					'content-type': 'application/json',
				},
				body: JSON.stringify({
					startDateTime: info.start,
					endDateTime: info.end
				})
			});

			if (!response.ok) throw new Error('수신 오류');
			const data = await response.json();

			data.forEach(v => {
				let endDate = new Date(v.endDate);
				endDate.setDate(endDate.getDate() + 1);
				calendar.addEvent({
					id: 'everyVacation' + v.vacationId,
					title: v.username, // FIXME: 사원 이름
					start: v.startDate,
					end: endDate,
					allDay: true,
					groupId: 'everyVacation',
					backgroundColor: '#fec400'
				});
			});
		} catch (error) {
			console.error(error)
		}
	}

	checkSchedule.addEventListener('change', function () {
		if (checkSchedule.checked == true) getSchedule(currentDateInfo);
		else {
			calendar.getEvents().forEach(event => {
				if (event.groupId == 'schedule') {
					event.remove();
				}
			})
		}
	})

	async function getSchedule(info) {
		try {
			const response = await fetch('/api/calendar/schedule', {
				method: 'POST',
				headers: {
					'content-type': 'application/json',
				},
				body: JSON.stringify({
					startDateTime: info.start,
					endDateTime: info.end
				})
			});

			if (!response.ok) throw new Error('수신 오류');
			const data = await response.json();

			data.forEach(s => {
				calendar.addEvent({
					id: 'schedule' + s.personalScheduleId,
					title: s.scheduleName,
					start: s.scheduleDateTime,
					end: s.scheduleDateTime,
					groupId: 'schedule',
					backgroundColor: '#0acb8e'
				});
			});
		} catch (error) {
			console.error(error)
		}
	}


});


/* primary		#9e6de0
 * secondary	#fd5190
 * success		#0acb8e
 * warning		#fec400
 */