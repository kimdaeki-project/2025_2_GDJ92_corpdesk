package com.goodee.corpdesk.home;

import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import com.goodee.corpdesk.attendance.DTO.ResAttendanceDTO;
import com.goodee.corpdesk.attendance.service.AttendanceService;
import com.goodee.corpdesk.board.Board;
import com.goodee.corpdesk.board.BoardService;
import com.goodee.corpdesk.email.EmailService;
import com.goodee.corpdesk.employee.EmployeeService;
import com.goodee.corpdesk.employee.ResEmployeeDTO;
import com.goodee.corpdesk.schedule.dto.ResPersonalScheduleDTO;
import com.goodee.corpdesk.vacation.dto.ResVacationDTO;
import com.goodee.corpdesk.vacation.service.VacationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class HomeController {

    @Autowired
    private HomeService homeService;
    @Autowired
    private VacationService vacationService;
    @Autowired
    private AttendanceService attendanceService;
	@Autowired
	private EmployeeService employeeService;
    @Autowired
    private BoardService boardService;

    @Value("${api.kakao.javascript.key}")
    private String appkey;

    @ModelAttribute("appkey")
    public String getAppkey() {
        return appkey;
    }

    @GetMapping("/")
	public String home(Authentication authentication) {
		if(authentication != null && authentication.isAuthenticated()) {
			return "redirect:/dashboard";
		}
		return "index";
	}

	@GetMapping("login/{msg}")
	public String login(@PathVariable String msg, Model model) {
		msg = msg.replaceAll("\\+", " ");
		model.addAttribute("msg", msg);
		return "index";
	}

	@GetMapping("/reset/password")
	public String resetPassword() {
		return "reset_password";
	}

	@GetMapping("/get-mail")
	@ResponseBody
	public String getEmail(String username) {
		return employeeService.getEmail(username);
	}

	@PostMapping("/reset-password")
	@ResponseBody
	public String resetPassword(String username) throws Exception {
		return employeeService.resetPassword(username);
	}

    @GetMapping("/dashboard")
    public String home(@AuthenticationPrincipal UserDetails userDetails
                       , Model model) throws Exception {
        
        String username = userDetails.getUsername();

        // 1. 직원 정보
        ResEmployeeDTO employee = homeService.getEmployee(username);
        model.addAttribute("employee", employee);

        // 2. 일정 정보
        ResPersonalScheduleDTO personalSchedule = homeService.getSchedule(username);
        model.addAttribute("personalSchedule", personalSchedule);

        // 3. 결재 정보
        ResApprovalDTO approval = homeService.getApproval(username);
        model.addAttribute("approval", approval);

        // 4. 연차
        // 잔여 연차
        ResVacationDTO vacation = vacationService.getVacation(username);
        model.addAttribute("remainingVacation", vacation.getRemainingVacation());

        // TODO 휴가
        model.addAttribute("vacation", vacation);

        // 5. 근태
        ResAttendanceDTO currAttd = attendanceService.getCurrentAttendance(username);
        model.addAttribute("currAttd", currAttd);

        // 6. 공지사항
        List<Board> notices = new ArrayList<>();
        Page<Board> page = boardService.getNoticeBoards(null);
        if (page != null) notices = page.getContent();

        model.addAttribute("notices", notices);

        return "dashboard";

    }
}
