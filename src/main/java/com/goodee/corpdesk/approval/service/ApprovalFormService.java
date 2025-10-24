package com.goodee.corpdesk.approval.service;

import com.goodee.corpdesk.approval.dto.ApproverDTO;
import com.goodee.corpdesk.approval.dto.ReqApprovalDTO;
import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import com.goodee.corpdesk.approval.entity.Approval;
import com.goodee.corpdesk.approval.entity.ApprovalForm;
import com.goodee.corpdesk.approval.entity.Approver;
import com.goodee.corpdesk.approval.repository.ApprovalFormRepository;
import com.goodee.corpdesk.approval.repository.ApprovalRepository;
import com.goodee.corpdesk.approval.repository.ApproverRepository;
import com.goodee.corpdesk.vacation.entity.VacationType;
import com.goodee.corpdesk.vacation.repository.VacationTypeRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class ApprovalFormService {

    @Autowired
	private ApprovalFormRepository approvalFormRepository;
    @Autowired
    private VacationTypeRepository vacationTypeRepository;

    public ResApprovalDTO getApprovalForm(Integer approvalId) throws Exception {
        Optional<ApprovalForm> result = approvalFormRepository.findById(approvalId);

        if(result.isEmpty())  return null;

        ApprovalForm approvalForm = result.get();

        return approvalForm.toResApprovalDTO();
    }

    public List<ResApprovalDTO> getApprovalFormList() throws Exception {
        List<ApprovalForm> result = approvalFormRepository.findAll();

        return result.stream().map(ApprovalForm::toResApprovalDTO).toList();
    }

    public List<ResApprovalDTO> getVacationTypeList() throws Exception {
        List<VacationType> result = vacationTypeRepository.findAllByUseYn(true);
        log.warn("VacationType list result: {}", result);

        return result.stream().map(VacationType::toResApprovalDTO).toList();
    }

}
