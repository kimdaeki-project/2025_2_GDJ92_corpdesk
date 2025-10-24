package com.goodee.corpdesk.approval.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import com.goodee.corpdesk.approval.entity.Approval;
import com.goodee.corpdesk.approval.service.ApprovalFormService;
import com.goodee.corpdesk.department.service.DepartmentService;
import com.goodee.corpdesk.employee.EmployeeService;
import com.goodee.corpdesk.employee.ResEmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.goodee.corpdesk.approval.dto.ApprovalDTO;
import com.goodee.corpdesk.approval.dto.ReqApprovalDTO;
import com.goodee.corpdesk.approval.service.ApprovalService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequestMapping(value = "/approval/**")
public class ApprovalController {

	@Value("${cat.approval}")
	private String cat;
    @Value("${app.upload.approval}")
    private String approvalPath;

    @ModelAttribute("cat")
	public String getCat() {
		return cat;
	}
    @ModelAttribute("approvalPath")
    public String getApprovalPath() {
        return approvalPath;
    }
    @ModelAttribute("userDetail")
    public ResEmployeeDTO getUserDetail(@AuthenticationPrincipal UserDetails userDetails) {

        ResEmployeeDTO userDetail = null;
        userDetail = employeeService.getFulldetail(userDetails.getUsername());
        if (userDetail == null) userDetail = new ResEmployeeDTO();

        return userDetail;

    }

    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private ApprovalFormService approvalFormService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private EmployeeService employeeService;


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

        return approvalFormService.getVacationTypeList();

    }
	
	// 결재 요청 
	@PostMapping("")
    @ResponseBody
	public ResApprovalDTO submit(ReqApprovalDTO reqApprovalDTO
                                 , @RequestParam(value = "files", required = false) MultipartFile[] files
                                 , @AuthenticationPrincipal UserDetails userDetails) throws Exception {

        String modifiedBy = userDetails.getUsername();

		return approvalService.createApproval(reqApprovalDTO, files, modifiedBy);
		
	}
	
	// 결재 요청 취소
	@DeleteMapping("{approvalId}")
    @ResponseBody
	public ResponseEntity<String> cancel(@PathVariable("approvalId") Long approvalId) throws Exception {

		ResponseEntity<String> result = approvalService.deleteApproval(approvalId);

        // TODO result 반환값에 따라 화면 표출 처리
		return result;
		
	}

    // 결재 수정 화면 요청
    @GetMapping("{approvalId}/edit")
    public String edit(@PathVariable("approvalId") Long approvalId,
                       @AuthenticationPrincipal UserDetails userDetails,
                       Model model) throws Exception {

        String username = userDetails.getUsername();
        ResApprovalDTO detail = approvalService.getApproval(approvalId);

        if(!username.equals(detail.getUsername())) throw new AccessDeniedException("접근 권한 없음");

        // 폼을 구성하는 정보들 바인딩
        // 0. 폼 정보 얻어오기
        ResApprovalDTO form = approvalFormService.getApprovalForm(detail.getApprovalFormId());
        model.addAttribute("form", form);

        // 1. 유저 정보 얻어오기
        ResEmployeeDTO userInfo = approvalService.getDetailWithDeptAndPosition(username);
        model.addAttribute("userInfo", userInfo);

        // 2. 결재 대상 부서의 정보 얻어오기
        ResApprovalDTO targetDept = departmentService.getDepartment(detail.getDepartmentId());
        model.addAttribute("targetDept", targetDept);

        // 3. 결재 대상 부서의 직원 목록 얻어오기 - 직원id, 직원이름, 부서명, 직위명, 파일정보
        List<ResApprovalDTO> employeeList = approvalService.getEmployeeWithDeptAndPositionAndFile(detail.getDepartmentId(), true);
        model.addAttribute("employeeList", employeeList);

        // 4. 기안일(오늘 날짜)
        model.addAttribute("today", LocalDate.now().toString());

        // 결재 내용 정보들 바인딩

        // approvalContent는 JSON 파싱해서 별도로 전달
        if (detail != null && detail.getApprovalContent() != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> approvalContentMap = mapper.readValue(
                    detail.getApprovalContent(),
                    new TypeReference<Map<String, String>>() {}
                );
                model.addAttribute("approvalContentMap", approvalContentMap);
            } catch (Exception e) {
                model.addAttribute("approvalContentMap", new HashMap<>());
            }
        }

        ResApprovalDTO approverInfo = approvalService.getAppover(approvalId,  username);

        model.addAttribute("detail", detail);
        model.addAttribute("approverInfo", approverInfo);

        model.addAttribute("edit", true);

        return "approval/add";

    }

    // TODO 결재 수정
	
	// 결재 승인/반려
	// 1. approvalId, approverId를 사용해 결재와 결재자 데이터를 받아와 수정하는 방식의 메서드
	@PatchMapping("{approvalId}")
    @ResponseBody
	public String process(@PathVariable("approvalId") String approvalId, @RequestBody ReqApprovalDTO reqApprovalDTO) throws Exception {

		System.err.println("process()");

        // reqApprovalDTO에는 approverId, approveYn가 있음
        log.warn("approvalId: {}", approvalId);
        log.warn("reqApprovalDTO: {}", reqApprovalDTO);
		String result = approvalService.processApproval(Long.parseLong(approvalId), reqApprovalDTO); // NOT_FOUND, PROCESSED 중 하나의 값이 반환됨
		log.info("result: {}", result);
		
		return result;
	}

    // TODO DTO에 부서 이름도 담아서 화면에 뿌리도록 수정
	// 특정 결재 목록 조회
    @GetMapping("list")
    public String getApprovalList(@RequestParam(value = "listType", required = false) String listType
                                  , @AuthenticationPrincipal UserDetails userDetails
                                  , Model model
                                  , @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) throws Exception {

        String username = userDetails.getUsername();

        // 2. 결재 목록 데이터 뿌리기
        if (listType == null || listType.equals("")) {
            List<ResApprovalDTO> tempList = approvalService.getApprovalList("temp", username);
            List<ResApprovalDTO> reqList = approvalService.getApprovalList("request", username);
            List<ResApprovalDTO> waitList = approvalService.getApprovalList("wait", username);
            List<ResApprovalDTO> storList = approvalService.getApprovalList("storage", username);

            model.addAttribute("tempList", tempList);
            model.addAttribute("reqList", reqList);
            model.addAttribute("waitList", waitList);
            model.addAttribute("storList", storList);
        } else {
            Page<ResApprovalDTO> result = approvalService.getAllApprovalList(listType, username, pageable);

            model.addAttribute("list", result);
            model.addAttribute("listType", listType);
        }

        return "approval/list";

    }

    // 결재 상세 조회
    @GetMapping("{approvalId}")
    public String getApproval(@PathVariable("approvalId") Long approvalId
                              , @AuthenticationPrincipal UserDetails userDetails
                              , Model model) throws Exception {

        String username = userDetails.getUsername();

        ResApprovalDTO detail = approvalService.getApproval(approvalId);

        // approvalContent는 JSON 파싱해서 별도로 전달
        if (detail != null && detail.getApprovalContent() != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> approvalContentMap = mapper.readValue(
                        detail.getApprovalContent(),
                        new TypeReference<Map<String, String>>() {}
                );
                model.addAttribute("approvalContentMap", approvalContentMap);
            } catch (Exception e) {
                model.addAttribute("approvalContentMap", new HashMap<>());
            }
        }

        ResApprovalDTO userInfo = approvalService.getDetail(username);
        ResApprovalDTO approverInfo = approvalService.getAppover(approvalId,  username);

        model.addAttribute("detail", detail);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("approverInfo", approverInfo);

        return "approval/detail";

    }
    
    // 첨부파일 삭제
    @DeleteMapping("file/{fileId}")
    @ResponseBody
    public ResponseEntity<String> deleteFile(@PathVariable("fileId") Long fileId,
                           @AuthenticationPrincipal UserDetails userDetails) throws Exception {

        String username = userDetails.getUsername();

        return approvalService.deleteFile(fileId, username);

    }
    
}
