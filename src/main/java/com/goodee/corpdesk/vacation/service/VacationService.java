package com.goodee.corpdesk.vacation.service;

import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeRepository;
import com.goodee.corpdesk.employee.EmployeeService;
import com.goodee.corpdesk.employee.ResEmployeeDTO;
import com.goodee.corpdesk.vacation.VacationManager;
import com.goodee.corpdesk.vacation.dto.ResVacationDTO;
import com.goodee.corpdesk.vacation.entity.Vacation;
import com.goodee.corpdesk.vacation.entity.VacationDetail;
import com.goodee.corpdesk.vacation.repository.VacationDetailRepository;
import com.goodee.corpdesk.vacation.repository.VacationRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class VacationService {

    @Autowired
    private VacationRepository vacationRepository;
    @Autowired
    private VacationDetailRepository vacationDetailRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private VacationManager vacationManager;
    @Autowired
    private EmployeeService employeeService;

    // 특정 월, 일에 해당하는 입사자들 데이터를 가져와서 vacation의 총 연차, 잔여 연차 update
    public Vacation updateVacationsByHireDate(LocalDate currDate) throws Exception {

        log.warn("start updateVacationsByHireDate");

        // 월, 일로 입사자들 데이터를 가져옴
        int month = currDate.getMonthValue();
        int day = currDate.getDayOfMonth();

        List<Employee> employeeList = employeeRepository.findAllByHireDateMonthDay(true, month, day);

        Vacation vacation = new Vacation();

        // 모든 입사자들에 대해 현재 총발생연차를 계산하고, DB상 총발생연차를 계산된 총발생연차로 대체
        // (계산된 총발생연차 - DB상 총발생연차)만큼 잔여연차를 증가시킴
        for (Employee employee : employeeList) {
            int newTotalVacation = vacationManager.calTotalVacation(employee.getHireDate());

            vacation = vacationRepository.findByUseYnAndUsername(true, employee.getUsername());
            if(vacation == null) {
                Vacation newVacation = new Vacation();
                newVacation.setUsername(employee.getUsername());
                newVacation.setTotalVacation(0);
                newVacation.setRemainingVacation(0);
                vacation = vacationRepository.save(newVacation);
                log.warn("newVacation: {}", newVacation);
                log.warn("insertedVacation: {}", vacation);
            }
            int oldTotalVacation = vacation.getTotalVacation();

            int increaseAmount = newTotalVacation - oldTotalVacation;

            if(increaseAmount > 0) {
                vacation.setTotalVacation(newTotalVacation);
                vacation.setRemainingVacation(vacation.getRemainingVacation() + increaseAmount);

                vacationRepository.save(vacation);
            }

        }

        return vacation;

    }

    public List<ResVacationDTO> getVacationDetails(Integer vacationId) throws Exception {

        List<VacationDetail> vacationDetails = new ArrayList<>();
        List<ResVacationDTO> resVacationDTOList = new ArrayList<>();

        if(vacationId == null) vacationDetails = vacationDetailRepository.findAllVacationDetailByUseYn(true);
        else vacationDetails = vacationDetailRepository.findAllVacationDetailByUseYnAndVacationId(true, vacationId);

        resVacationDTOList = vacationDetails.stream().map(VacationDetail::toResVacationDTO).toList();

        for(ResVacationDTO dto : resVacationDTOList) {
            if(dto.getVacationId() == null) continue;

            Optional<Vacation> vacationOpt = vacationRepository.findById(dto.getVacationId());
            log.warn("vacationOpt: {}", vacationOpt);

            if(vacationOpt.isPresent()) {
                String username = vacationOpt.get().getUsername();

                ResEmployeeDTO employeeDetail = employeeService.getFulldetail(username);
                dto.setName(employeeDetail.getName());
                dto.setDepartment(employeeDetail.getDepartmentName());
                dto.setPosition(employeeDetail.getPositionName());
            }
        }

        if(resVacationDTOList == null || resVacationDTOList.isEmpty()) return resVacationDTOList; // 조회 결과 없으면 값 없는 껍데기 객체 반환

        log.warn("resVacationDTOList: {}", resVacationDTOList);

        return resVacationDTOList;
    }

    public ResVacationDTO getVacation(String username) throws Exception {

        Vacation vacation = vacationRepository.findByUseYnAndUsername(true, username);

        if(vacation == null) return new ResVacationDTO();

        return vacation.toResVacationDTO();

    }

    public List<ResVacationDTO> getVacationDetails(Integer vacationId, Integer vacationTypeId) throws Exception {

        List<VacationDetail> vacationDetails = new ArrayList<>();
        List<ResVacationDTO> resVacationDTOList = new ArrayList<>();

        if(vacationId == null) vacationDetails = vacationDetailRepository.findAllVacationDetailByUseYnAndVacationTypeId(true, vacationTypeId);
        else vacationDetails = vacationDetailRepository.findAllVacationDetailByUseYnAndVacationIdAndVacationTypeId(true, vacationId, vacationTypeId);

        resVacationDTOList = vacationDetails.stream().map(VacationDetail::toResVacationDTO).toList();

        if(resVacationDTOList == null || resVacationDTOList.isEmpty()) return resVacationDTOList; // 조회 결과 없으면 값 없는 껍데기 객체 반환

        return resVacationDTOList;
    }

}
