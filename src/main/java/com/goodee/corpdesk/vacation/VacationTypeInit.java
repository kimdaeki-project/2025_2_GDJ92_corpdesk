package com.goodee.corpdesk.vacation;

import com.goodee.corpdesk.employee.Role;
import com.goodee.corpdesk.employee.RoleRepository;
import com.goodee.corpdesk.vacation.entity.VacationType;
import com.goodee.corpdesk.vacation.repository.VacationTypeRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VacationTypeInit implements InitializingBean {

	@Autowired
	private VacationTypeRepository vacationTypeRepository;

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}

	private void init() {
//        VacationType type1 = new VacationType();
//        type1.setVacationTypeId(1);
//        type1.setVacationTypeName("연차휴가");
//        type1.setUseYn(true);
//        vacationTypeRepository.save(type1);
//
//        VacationType type2 = new VacationType();
//        type2.setVacationTypeId(2);
//        type2.setVacationTypeName("병가");
//        type2.setUseYn(true);
//        vacationTypeRepository.save(type2);
//
//        VacationType type3 = new VacationType();
//        type3.setVacationTypeId(3);
//        type3.setVacationTypeName("경조사휴가");
//        type3.setUseYn(true);
//        vacationTypeRepository.save(type3);
//
//        VacationType type4 = new VacationType();
//        type4.setVacationTypeId(4);
//        type4.setVacationTypeName("출산휴가");
//        type4.setUseYn(true);
//        vacationTypeRepository.save(type4);
//
//        VacationType type5 = new VacationType();
//        type5.setVacationTypeId(5);
//        type5.setVacationTypeName("육아휴직");
//        type5.setUseYn(true);
//        vacationTypeRepository.save(type5);
//
//        VacationType type6 = new VacationType();
//        type6.setVacationTypeId(6);
//        type6.setVacationTypeName("공가");
//        type6.setUseYn(true);
//        vacationTypeRepository.save(type6);
	}
}
