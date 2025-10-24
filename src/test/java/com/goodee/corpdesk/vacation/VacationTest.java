package com.goodee.corpdesk.vacation;

import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeRepository;
import com.goodee.corpdesk.vacation.entity.Vacation;
import com.goodee.corpdesk.vacation.entity.VacationDetail;
import com.goodee.corpdesk.vacation.repository.VacationDetailRepository;
import com.goodee.corpdesk.vacation.repository.VacationRepository;
import com.goodee.corpdesk.vacation.service.VacationService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class VacationTest {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private VacationRepository vacationRepository;
    @Autowired
    private VacationDetailRepository vacationDetailRepository;
    @Autowired
    private VacationService vacationService;

    @Test
    public void createVacationTest() throws Exception {

        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);

        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            vacationService.updateVacationsByHireDate(current);

            current = current.plusDays(1);
        }

    }

    @Test
    public void deleteVacationTest() throws Exception {

        List<Vacation> vacations = vacationRepository.findAll();

        for(Vacation v : vacations){
            if(v.getUsername() == null) continue;

            Optional<Employee> employee = employeeRepository.findById(v.getUsername());

            if(!employee.isPresent() || !employee.get().getUseYn()) {
                v.setUseYn(false);
                vacationRepository.save(v);
            }
        }

    }

    @Test
    public void deleteVacationDetailTest() throws Exception {
        List<VacationDetail> vacationDetails = vacationDetailRepository.findAll();

        for(VacationDetail vd : vacationDetails){
            if(vd.getVacationId() == null) continue;

            Optional<Vacation> v = vacationRepository.findById(vd.getVacationId());
            if(!v.isPresent() || !v.get().getUseYn()) {
                vd.setUseYn(false);
                vacationDetailRepository.save(vd);
            }
        }
    }

}
