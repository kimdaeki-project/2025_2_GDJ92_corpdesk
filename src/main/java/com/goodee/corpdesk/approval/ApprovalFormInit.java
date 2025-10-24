package com.goodee.corpdesk.approval;

import com.goodee.corpdesk.approval.entity.ApprovalForm;
import com.goodee.corpdesk.approval.repository.ApprovalFormRepository;
import com.goodee.corpdesk.vacation.entity.VacationType;
import com.goodee.corpdesk.vacation.repository.VacationTypeRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApprovalFormInit implements InitializingBean {

	@Autowired
	private ApprovalFormRepository approvalFormRepository;

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}

	private void init() {
//        ApprovalForm form1 = new ApprovalForm();
//        form1.setApprovalFormId(1);
//        form1.setFormTitle("휴가 신청");
//        form1.setUseYn(true);
//        approvalFormRepository.save(form1);
//
//        ApprovalForm form2 = new ApprovalForm();
//        form2.setApprovalFormId(2);
//        form2.setFormTitle("출장 신청");
//        form2.setUseYn(true);
//        approvalFormRepository.save(form2);
//
//        ApprovalForm form3 = new ApprovalForm();
//        form3.setApprovalFormId(3);
//        form3.setFormTitle("업무 기안");
//        form3.setUseYn(true);
//        approvalFormRepository.save(form3);
	}

}
