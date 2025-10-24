package com.goodee.corpdesk.approval.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodee.corpdesk.approval.dto.*;
import com.goodee.corpdesk.approval.entity.ApprovalForm;
import com.goodee.corpdesk.approval.repository.ApprovalFormRepository;
import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.department.repository.DepartmentRepository;
import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeRepository;
import com.goodee.corpdesk.employee.ResEmployeeDTO;
import com.goodee.corpdesk.notification.entity.Notification;
import com.goodee.corpdesk.notification.service.NotificationService;
import com.goodee.corpdesk.file.FileManager;
import com.goodee.corpdesk.file.dto.FileDTO;
import com.goodee.corpdesk.file.entity.ApprovalFile;
import com.goodee.corpdesk.file.repository.ApprovalFileRepository;
import com.goodee.corpdesk.vacation.entity.Vacation;
import com.goodee.corpdesk.vacation.entity.VacationDetail;
import com.goodee.corpdesk.vacation.repository.VacationDetailRepository;
import com.goodee.corpdesk.vacation.repository.VacationRepository;

import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.goodee.corpdesk.approval.entity.Approval;
import com.goodee.corpdesk.approval.entity.Approver;
import com.goodee.corpdesk.approval.repository.ApprovalRepository;
import com.goodee.corpdesk.approval.repository.ApproverRepository;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional
public class ApprovalService {

    @Value("${app.upload}")
    private String upload;

    @Value("${app.upload.approval}")
    private String approvalPath;

	@Autowired
	private ApprovalRepository approvalRepository;
	@Autowired
	private ApproverRepository approverRepository;
    @Autowired
    private ApprovalFormRepository approvalFormRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private VacationRepository vacationRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private VacationDetailRepository vacationDetailRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private FileManager fileManager;
    @Autowired
    private ApprovalFileRepository approvalFileRepository;
    @Autowired
    private ApprovalFileRepository addApprovalFileRepository;

    // 반환값 종류
    // ResApprovalDTO: approval 혹은 approval과 approver insert 성공, approval의 정보만 반환
	// Exception: approval 혹은 approval의 조회 결과가 없거나 insert 실패
	public ResApprovalDTO createApproval(ReqApprovalDTO reqApprovalDTO, MultipartFile[] files, String modifiedBy) throws Exception {
        // 1. 결재 내용에 insert
		Approval approval = reqApprovalDTO.toApprovalEntity();
		approval.setModifiedBy(modifiedBy);
		approval = approvalRepository.save(approval); // 조회 결과가 없다면 예외가 터지고 롤백됨

        ResApprovalDTO resApprovalDTO = approval.toResApprovalDTO();
        // 2. 파일 저장
        if (files != null && files.length > 0) {
            for (MultipartFile a : files) {
                log.warn("file: {}", a.getName());

                // a에 실질적으로 파일이 들어있지 않다면 파일 저장 로직을 진행하지 않음
                if (a.getSize() <= 0) continue;

                // 1. file을 HDD에 저장하고 saveName을 받아옴
                FileDTO fileDTO = fileManager.saveFile(upload + approvalPath, a);
                ApprovalFile approvalFile = fileDTO.toApprovalFile();
                approvalFile.setApprovalId(approval.getApprovalId());
                approvalFile.setModifiedBy(modifiedBy);
                // 2. DB에 데이터 저장
                approvalFileRepository.save(approvalFile);
            }
        }

		// 3. 결재자에 insert
        // 결재자 정보가 없다면
        // - 임시저장인 경우 -> 바로 return
        // - 결재요청인 경우 -> 승인 상태로 처리 후 return
        if (reqApprovalDTO.getApproverDTOList() == null || reqApprovalDTO.getApproverDTOList().isEmpty()) {
            if(!"T".equalsIgnoreCase(reqApprovalDTO.getStatus() + "")) {
                approval = approvalRepository.save(approval);
                reqApprovalDTO.setApproveYn('Y');
                String result = processApproval(approval.getApprovalId(), reqApprovalDTO);
                log.warn("result: {}", result);
            }

            resApprovalDTO.setApprovalId(approval.getApprovalId());

            return resApprovalDTO;
        }
		for (ApproverDTO approverDTO : reqApprovalDTO.getApproverDTOList()) {
			Approver approver = approverDTO.toEntity();
			approver.setApprovalId(approval.getApprovalId());
			approver.setModifiedBy(modifiedBy);
			
			// 만약 승인 순서가 1이 아니면 use_yn값을 false로 하여 insert
			if(approver.getApprovalOrder() != 1) {approver.setUseYn(false);
			}else {
				notificationService.reqNotification(approval.getApprovalId(),approval.getApprovalFormId(),approval.getUsername(),"approval", "새로운 결재 요청이 있습니다.",approver.getUsername());
			}
			approverRepository.save(approver); // 조회 결과가 없다면 예외가 터지고 롤백됨
		}

		// 4. DTO 반환 (approverDTOList()는 null인 채로 반환됨)
        resApprovalDTO.setApprovalId(approval.getApprovalId());
        return resApprovalDTO;
		
	}
	 
	// 반환값 종류
    // DENIED: 결재가 반려 처리됨
    // PROCESSED: 결재가 승인됨
    // NOT_FOUND: approvalId에 해당하는 Approval이 없음
    // Exception: update 실패
	public ResponseEntity<String> deleteApproval(Long approvalId) throws Exception {

		// 1. 결재 use_yn false로 update
		Optional<Approval> result = approvalRepository.findById(approvalId); // id로 결재 정보를 조회해 옴

        // 삭제할 결재 정보가 없으면 이후의 삭제 로직을 수행할 필요 없으므로 바로 return
		if (result.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);;

		// 삭제할 결재 정보가 있으면 삭제 로직 진행...
		// 결재가 완료되었는가? (결재 상태가 y/n인가?)
		// -> true -> 결재 취소 거부
		// -> false -> 결재 취소 진행
		Approval approval = result.get();
		if (approval.getStatus() == 'Y' || approval.getStatus() == 'N') return new ResponseEntity<>(HttpStatus.CONFLICT); // CONFLICT: 이미 완료된 리소스에 대한 수정을 거부
		else approval.setUseYn(false);
		
		// 2. 결재자들 use_yn false로 update
		List<Approver> approverList = approverRepository.findAllByApprovalId(approvalId);
	     // (추가) 현재 결재중인 결재자에게 알림 전송
	        if (approval.getStatus() == 'w') {
	        	approverList.forEach(approver->{
	        		if(approver.getUseYn()) {
	        			try {
							notificationService.reqNotification(approvalId,approval.getApprovalFormId(),approval.getUsername(),"approval", "결재 문서가 기안자에 의해 취소되었습니다.",approver.getUsername());
						} catch (Exception e) {
							e.printStackTrace();
						}
	        		}
	        	});
	        }
        approverList.forEach(approver -> approver.setUseYn(false));
		
		return new ResponseEntity<>(HttpStatus.OK);
	} 
	
    // 해당 결재의 결재자 정보의 approve_yn 정보를 approveYn값으로 수정한 다음,
    // 그 다음 승인 순서인 결재자 정보(만약 있다면)의 use_yn값을 true로 수정
    // 반환값 종류
    // NOT_FOUND: 수정할 결재자 정보 혹은 수정할 결재 정보가 없음
    // PROCESSED: 결재 반려/승인이 처리됨
    // Exception: 그 외 예외상황
	public String processApproval(Long approvalId, ReqApprovalDTO reqApprovalDTO) throws Exception {
        
        // 0. DTO에서 approverId, approveYn를 받아옴
        log.warn("{}", reqApprovalDTO);
        Long approverId = reqApprovalDTO.getApproverId();
        System.err.println("approverId = " + approverId);
        String approveYn = reqApprovalDTO.getApproveYn().toString();
        System.err.println("approveYn = " + approveYn);

        // 1. 해당 결재의 결재자 정보의 approve_yn 정보를 approveYn값으로 수정
		// 결재자 정보 중 approval_id = approvalId 이고 approver_id = approverId 인 정보 select
		Approver result = approverRepository.findByApprovalIdAndApproverId(approvalId, approverId);
		
		// 수정할 결재자 정보가 있으면 수정
		log.warn("1: {}", result);
		if (result != null) result.setApproveYn(approveYn.charAt(0));
		
		// 결재자가 결재를 반려했다면(approveYn = n) 결재 상태의 값을 반려로 수정
		if(approveYn.equalsIgnoreCase("N")) {
			// 결재 정보 조회
			Optional<Approval> result2 = approvalRepository.findById(approvalId);
			log.warn("2: {}", result2);
			
			// approval이 null이 아닐 때만 다음 로직 진행
			if(result2.isEmpty()) return "NOT_FOUND";
			Approval approval = result2.get();
			
			// 결재 상태 수정
			approval.setStatus('N');
			//기안자에게 알림전송
			notificationService.reqNotification(approval.getApprovalId(),approval.getApprovalFormId(),approval.getUsername(),
					"approval", "결재요청이 반려되었습니다.",approval.getUsername());
		}
		// 결재자가 승인을 했다면 (approveYn = y) 아래 2번 로직 진행
		if(approveYn.equalsIgnoreCase("Y")) {
			// 2. 다음 승인자가 있는가?
			// false -> 결재 상태의 값을 승인으로 수정
			// true -> 결재 상태 수정없음(대기), 그 다음 승인 순서인 결재자 정보(만약 있다면)의 use_yn값을 true로 수정
			
			// 그 다음 승인 순서인 결재자 정보 조회
            Approver nextApprover = null;
            if(result != null) nextApprover = approverRepository.findByApprovalIdAndApprovalOrder(approvalId, result.getApprovalOrder() + 1);
			log.warn("3: {}", nextApprover);
			
			// 수정할 결재자 정보가 없으면 결재 상태의 값을 승인으로&use_yn=true로 수정하고 return
			if (nextApprover == null) {
                log.warn("nextApprover is null");

				// 결재 정보 조회
				Optional<Approval> result2 = approvalRepository.findById(approvalId);
				
				// approval이 null이 아닐 때만 다음 로직 진행
				if(result2.isEmpty()) return "NOT_FOUND";
				Approval approval = result2.get();
				
				// 결재 상태 수정
				approval.setStatus('Y');
				// 결재 승인 알림
				notificationService.reqNotification(approval.getApprovalId(),approval.getApprovalFormId(),approval.getUsername(),
						"approval", "결재요청이 승인되었습니다.",approval.getUsername());

                // 추가) 결재 유형이 휴가라면 vacation_datail에 데이터 insert & vacaion의 사용연차, 총연차 update
                if(approval.getApprovalFormId() == 1) {
                    // 1) vacation을 username으로 가져옴
                    Vacation vacation = vacationRepository.findByUseYnAndUsername(true, approval.getUsername());

                    // 2) vacation_datail insert
                    // 휴가번호 = vacationId
                    // 결재번호 = approvalId
                    // 휴가유형번호, 시작날짜, 종료날짜, 휴가일수 -> approvalContent에서 가져옴
                    VacationDetail newVacationDetail = new VacationDetail();

                    newVacationDetail.setVacationId(vacation.getVacationId());
                    newVacationDetail.setApprovalId(approval.getApprovalId());

                    // JSON 문자열을 파싱해서 vacation_detail에 저장
                    JsonNode contentNode = objectMapper.readTree(approval.getApprovalContent());

                    newVacationDetail.setVacationTypeId(contentNode.get("vacationTypeId").asInt());
                    LocalDate startDate = LocalDate.parse(contentNode.get("startDate").asText());
                    newVacationDetail.setStartDate(startDate);
                    LocalDate endDate = LocalDate.parse(contentNode.get("endDate").asText());
                    newVacationDetail.setEndDate(endDate);
                    int usedDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
                    newVacationDetail.setUsedDays(usedDays);
                    vacationDetailRepository.save(newVacationDetail);

                    // 3) vacation update
                    // vacation_detail의 휴가일수만큼 사용연차 증가 & 잔여연차 감소
                    vacation.setUsedVacation(vacation.getUsedVacation() + usedDays);
                    vacation.setRemainingVacation(vacation.getRemainingVacation() - usedDays);
                    vacationRepository.save(vacation);
                }

			} else {
                log.warn("nextApprover is not null");

				// 수정할 결재자 정보가 있으면 결재 상태의 값을 수정하지 않고 그 다음 승인 순서인 결재자 정보(만약 있다면)의 use_yn값을 true로 수정
				nextApprover.setUseYn(true);
				
				// 결재 정보 조회
				Optional<Approval> result2 = approvalRepository.findById(approvalId);
				
				// approval이 null이 아닐 때만 다음 로직 진행
				if(result2.isEmpty()) return "NOT_FOUND";
				Approval approval = result2.get();
				
				// 결재 요청 알림
                notificationService.reqNotification(approval.getApprovalId(), approval.getApprovalFormId(), approval.getUsername(),
                    "approval", "새로운 결재 요청이있습니다.", nextApprover.getUsername());
                System.err.println(nextApprover);
			}
			
		}
		
		return "PROCESSED";
	}
    
    public Page<ResApprovalDTO> getAllApprovalList(String listType, String username, Pageable pageable) throws Exception {

        Page<ResApprovalDTO> result = null;
        switch (listType) {
            case "temp" -> result = approvalRepository.findApprovalSummaryByStatus(true, username, List.of("T"), pageable); // 내 결재 임시 보관함
            case "request" -> result = approvalRepository.findApprovalSummaryByStatus(true, username, List.of("W", "N", "Y"), pageable); // 내 결재 요청 목록
            case "wait" -> result = approvalRepository.findSummaryByApproverWithApproveYnAndStatus(true, username, List.of("W"), List.of("W"), pageable); // 결재 대기 목록
            case "storage" -> result = approvalRepository.findSummaryByApproverWithApproveYnAndStatus(true, username, List.of("Y", "N"), List.of("W", "Y", "N"), pageable); // 내가 결재한 목록
        }

        if(result == null) return new PageImpl<ResApprovalDTO>(List.of());

        return result;

    }

    public List<ResApprovalDTO> getApprovalList(String listType, String username) throws Exception {

        List<ResApprovalDTO> result = null;
        switch (listType) {
            case "temp" -> result = approvalRepository.findApprovalSummaryByStatus(
                                                    true, username, List.of("T"), 10L
                                                        ); // 내 결재 임시 보관함
            case "request" -> result = approvalRepository.findApprovalSummaryByStatus(
                                                    true, username, List.of("W", "N", "Y"), 10L
                                                        ); // 내 결재 요청 목록
            case "wait" -> result = approvalRepository.findSummaryByApproverWithApproveYnAndStatus(
                                                    true, username, List.of("W"), List.of("W"), 10L
                                                        ); // 결재 대기 목록
            case "storage" -> result = approvalRepository.findSummaryByApproverWithApproveYnAndStatus(
                                                    true, username, List.of("Y", "N"), List.of("W", "Y", "N"), 10L
                                                        ); // 내가 결재한 목록
        }

        if(result == null) return List.of();

        return result;

    }

    public ResApprovalDTO getApproval(Long approvalId) throws Exception {
        // 1. 결재 + 첨무파일 + 결재 양식 + 부서 + 기안자 조회
        // 결재 조회
        Optional<Approval> result = approvalRepository.findById(approvalId);

        if(result.isEmpty())  return null;

        Approval approval = result.get();
        ResApprovalDTO resApprovalDTO = approval.toResApprovalDTO();

        // 결재가 승인/반려 상태면 처리여부를 true로 설정
        List<Character> yOrN = List.of('y', 'Y', 'n', 'N');
        if(yOrN.contains(approval.getStatus())) resApprovalDTO.setProcessed(true);

        // 첨부파일 조회
        List<ApprovalFileDTO> files = approvalFileRepository.findAllByApprovalIdAndUseYn(approval.getApprovalId(), true)
                                                            .stream().map(ApprovalFile::toApprovalFileDTO).toList();
        resApprovalDTO.setFiles(files);

        // 결재 양식 조회
        ApprovalForm result3 = approvalFormRepository.findById(approval.getApprovalFormId()).get();
        resApprovalDTO.setApprovalFormId(result3.getApprovalFormId());
        resApprovalDTO.setFormTitle(result3.getFormTitle());

        // 부서 조회
        Department result4 = departmentRepository.findById(approval.getDepartmentId()).get();
        resApprovalDTO.setDepartmentName(result4.getDepartmentName());

        // 기안자 조회
        Employee result5 = employeeRepository.findById(approval.getUsername()).get();
        resApprovalDTO.setName(result5.getName());

        // 2. 결재자 조회
        List<ApproverDTO> approverDTOList = approverRepository.findByApprovalIdWithEmployeeAndDepartment(approvalId);

        if(approverDTOList.isEmpty())  return resApprovalDTO; // 결재자가 없으면? approval만 DTO에 담아서 return

        for(ApproverDTO approverDTO : approverDTOList){
            // 결재자 중 한 명이라도 승인/반려 처리를 했다면 처리여부를 true로 설정
            if(yOrN.contains(approverDTO.getApproveYn())) resApprovalDTO.setProcessed(true);
        }

        // 3. 결재와 결재자를 DTO에 담아 반환
        resApprovalDTO.setApproverDTOList(approverDTOList);

        return resApprovalDTO;
    }

    public ResEmployeeDTO getDetailWithDeptAndPosition(String username) {
        return employeeRepository.findEmployeeWithDeptAndPosition(true, username);
    }

    public List<ResApprovalDTO> getEmployeeWithDeptAndPositionAndFile(Integer departmentId, Boolean useYn) {
        return employeeRepository.findEmployeeWithDeptAndPositionAndFile(departmentId, useYn);
    }

    public ResApprovalDTO getDetail(String username) {
        return employeeRepository.findByUsername(username).get().toResApprovalDTO();
    }

    public ResApprovalDTO getAppover(Long approvalId, String username) {
        Approver approver = approverRepository.findAllByApprovalIdAndUsername(approvalId, username);

        if(approver == null)  return null;

        return approver.toResApprovalDTO();
    }

    public ResponseEntity<String> deleteFile(Long fileId, String username) throws Exception {

        Optional<ApprovalFile> opt = approvalFileRepository.findById(fileId);

        if (opt.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        ApprovalFile approvalFile = opt.get();

        if(!username.equals(approvalFile.getModifiedBy())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        // DB에서 useYn=false 처리
        approvalFile.setUseYn(false);
        approvalFileRepository.save(approvalFile);

        // HDD에서 삭제
        FileDTO fileDTO = new FileDTO();
        fileDTO.setSaveName(approvalFile.getSaveName());
        fileDTO.setExtension(approvalFile.getExtension());
        fileManager.deleteFile(upload + approvalPath, fileDTO);

        return new ResponseEntity<>(HttpStatus.OK);

    }

}
