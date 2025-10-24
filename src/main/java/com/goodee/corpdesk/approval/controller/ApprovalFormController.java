package com.goodee.corpdesk.approval.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodee.corpdesk.approval.dto.ReqApprovalDTO;
import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import com.goodee.corpdesk.approval.service.ApprovalFormService;
import com.goodee.corpdesk.approval.service.ApprovalService;
import com.goodee.corpdesk.department.service.DepartmentService;
import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeRepository;
import com.goodee.corpdesk.employee.EmployeeService;
import com.goodee.corpdesk.employee.ResEmployeeDTO;
import com.goodee.corpdesk.vacation.entity.VacationType;
import com.goodee.corpdesk.vacation.repository.VacationTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping(value = "/approval-form/**")
public class ApprovalFormController {

    @Autowired
    private ApprovalFormService approvalFormService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private EmployeeService employeeService;

	@Value("${cat.approval}")
	private String cat;
    @Autowired
    private ApprovalService approvalService;

    @ModelAttribute("cat")
	public String getCat() {
		return cat;
	}
    @ModelAttribute("userDetail")
    public ResEmployeeDTO getUserDetail(@AuthenticationPrincipal UserDetails userDetails) {

        ResEmployeeDTO userDetail = null;
        userDetail = employeeService.getFulldetail(userDetails.getUsername());
        if (userDetail == null) userDetail = new ResEmployeeDTO();

        return userDetail;

    }

    // 결재 양식 데이터 뿌리기
    @ModelAttribute("formList")
    public List<ResApprovalDTO> getFormList() throws  Exception {
        return approvalFormService.getApprovalFormList();
    }

    // 부서 목록 데이터 뿌리기
    @ModelAttribute("departmentList")
    public List<ResApprovalDTO> getDepartmentList() throws  Exception {
        return departmentService.getApprovalFormList();
    }

    // 휴가 목록 데이터 뿌리기
    @ModelAttribute("vacationTypeList")
    public List<ResApprovalDTO> getVacationTypeList() throws  Exception {
        log.warn("getVacationTypeList(): {}", approvalFormService.getVacationTypeList());
        return approvalFormService.getVacationTypeList();
    }

    // 결재 폼 조회
    @GetMapping("{formId}")
    public String getApproval(@PathVariable("formId") Integer formId
        , @RequestParam(value = "departmentId", required = false) Integer departmentId
        , @AuthenticationPrincipal UserDetails userDetails
        , Model model) throws Exception {

        String username = userDetails.getUsername();

        if(departmentId == null) {
            departmentId = employeeService.getFulldetail(username).getDepartmentId();
        }

        // 0. 폼 정보 얻어오기
        ResApprovalDTO form = approvalFormService.getApprovalForm(formId);

        // 1. 유저 정보 얻어오기
        ResEmployeeDTO userInfo = approvalService.getDetailWithDeptAndPosition(username);

        // 2. 결재 대상 부서의 정보 얻어오기
        ResApprovalDTO targetDept = departmentService.getDepartment(departmentId);

        // 3. 결재 대상 부서의 직원 목록 얻어오기 - 직원id, 직원이름, 부서명, 직위명, 파일정보
        List<ResApprovalDTO> employeeList = approvalService.getEmployeeWithDeptAndPositionAndFile(departmentId, true);

        // 3. model에 폼이랑 departmentId 바인딩
        model.addAttribute("form", form);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("targetDept", targetDept);
        model.addAttribute("today", LocalDate.now().toString());
        model.addAttribute("employeeList", employeeList);

        return "approval/add";

    }
    
}
