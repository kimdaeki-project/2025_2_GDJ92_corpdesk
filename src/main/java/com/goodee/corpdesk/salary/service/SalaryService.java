package com.goodee.corpdesk.salary.service;

import com.goodee.corpdesk.attendance.entity.Attendance;
import com.goodee.corpdesk.attendance.repository.AttendanceRepository;
import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeInfoDTO;
import com.goodee.corpdesk.employee.EmployeeRepository;
import com.goodee.corpdesk.salary.dto.EmployeeSalaryDTO;
import com.goodee.corpdesk.salary.entity.Allowance;
import com.goodee.corpdesk.salary.entity.Deduction;
import com.goodee.corpdesk.salary.entity.SalaryPayment;
import com.goodee.corpdesk.salary.repository.AllowanceRepository;
import com.goodee.corpdesk.salary.repository.DeductionRepository;
import com.goodee.corpdesk.salary.repository.SalaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SalaryService {

	private final SalaryRepository salaryRepository;
	private final AllowanceRepository allowanceRepository;
	private final DeductionRepository deductionRepository;
	private final EmployeeRepository employeeRepository;
	private final AttendanceRepository attendanceRepository;

	@Value("${deduction.pension.national}")
	private Double nationalPension;
	@Value("${deduction.insurance.health}")
	private Double healthInsurance;
	@Value("${deduction.insurance.nursing}")
	private Double nursingInsurance;
	@Value("${deduction.insurance.employment}")
	private Double employmentInsurance;

	@Value("${allowance.work.hours}")
	private Integer fixedWorkHours;

	public PagedModel<EmployeeSalaryDTO> getList(Pageable pageable) {
		Page<EmployeeSalaryDTO> page = salaryRepository.findAllEmployeeSalary(pageable);

		return new PagedModel<>(page);
	}

	public Map<String, Object> getDetail(Long paymentId) {
		Map<String, Object> map = new HashMap<>();
		SalaryPayment salaryPayment = salaryRepository.findById(paymentId).get();
		List<Allowance> allowanceList = allowanceRepository.findAllByPaymentId(paymentId);
		List<Deduction> deductionList = deductionRepository.findAllByPaymentId(paymentId);

		EmployeeInfoDTO employee = employeeRepository.findByIdWithDept(salaryPayment.getUsername()).get();

		map.put("salaryPayment", salaryPayment);
		map.put("allowanceList", allowanceList);
		map.put("deductionList", deductionList);

		map.put("employee", employee);

		return map;
	}

	// 스케줄러에서 호출
	// 급여 입력
	public void saveSalaries() {
		List<Employee> employeeList = employeeRepository.findAllByUseYnTrue();

		for (Employee e : employeeList) {
			SalaryPayment salaryPayment = new SalaryPayment();
			salaryPayment.setUsername(e.getUsername());
			salaryPayment.setPaymentDate(LocalDateTime.now());
			salaryPayment.setBaseSalary(e.getCurrentBaseSalary());

			SalaryPayment result = salaryRepository.save(salaryPayment);

			// 총 임금 : 기본급 + 수당
			AtomicLong totalPayment = new AtomicLong(); // 객체 안에 데이터를 저장

			totalPayment.set(result.getBaseSalary());

			// 수당
			// NOTE: attendance 정보 가져오기
			/* 해당 기간의 attendance 정보를 순회하면서 수당 정보 저장하기
			 * 연장 근로 수당
			 * 휴일 근로 수당
			 * 야간 근로 수당
			 */
			List<Allowance> allowanceList = this.getWorkAllowanceList(salaryPayment, totalPayment);
			allowanceRepository.saveAll(allowanceList);

			// NOTE: vacation 정보 가져오기 (연차 수당 폐기)
			/* 1년 전의 기록을 순회하면서 연차 수당 정보 저장하기
			 * 연차 수당
			 */

			// 공제
			/* 총 임금 기준으로 계산
			 * 국민연금 4.5%
			 * 건강보험 3.545%
			 * 장기요양보험 6.405% (건강 보험 中)
			 * 고용보험 0.9%
			 */
			List<Deduction> deductionList = this.getDeductionList(result.getPaymentId(), totalPayment);
			deductionRepository.saveAll(deductionList);
		}
	}

	// 근로수당
	private List<Allowance> getWorkAllowanceList(SalaryPayment salaryPayment, AtomicLong totalPayment) {
		// 근로 수당 목록을 추가하고 반환
		List<Allowance> allowanceList = new ArrayList<>();

		// 출퇴근 목록
		List<Attendance> attendanceList = attendanceRepository.findAllByUsernameAndMonth(salaryPayment.getUsername());

		// 통상 임금
		Long avgSalary = salaryPayment.getBaseSalary() / 209;

		for (Attendance a : attendanceList) {
			LocalDateTime checkIn = a.getCheckInDateTime();
			LocalDateTime checkOut = a.getCheckOutDateTime();

			if (checkIn == null || checkOut == null) continue;

			// 총 근로 시간 (소수점으로 분단위까지)
			Double workHours = (double) Duration.between(checkIn, checkOut).toMinutes() / 60;
			// 연장근로
			if (workHours > fixedWorkHours) {
				this.saveAllowance(salaryPayment.getPaymentId(), "연장근로수당",
						avgSalary, workHours - fixedWorkHours, totalPayment, allowanceList);
			}

			// 휴일근로
			if (a.getIsHoliday() == 'Y' || a.getIsHoliday() == 'y') {
				this.saveAllowance(salaryPayment.getPaymentId(), "휴일근로수당",
						avgSalary, workHours, totalPayment, allowanceList);
			}

			// 야간근로
			if (!checkIn.toLocalDate().isEqual(checkOut.toLocalDate())
					|| checkIn.isBefore(checkIn.withHour(06).withMinute(00).withSecond(00))
					|| checkOut.isAfter(checkOut.withHour(22).withMinute(00).withSecond(00))) {

				LocalDateTime start = checkIn;
				LocalDateTime end = checkOut;

				// 야간 근로 해당 범위만 산정
				if (checkIn.isBefore(checkIn.withHour(22).withMinute(00).withSecond(00))
						&& checkIn.isAfter(checkIn.withHour(06).withMinute(00).withSecond(00))) {
					start = checkIn.withHour(22).withMinute(00).withSecond(00);
				}

				if (checkOut.isAfter(checkOut.withHour(06).withMinute(00).withSecond(00))
						&& checkOut.isBefore(checkOut.withHour(22).withMinute(00).withSecond(00))) {
					end = checkOut.withHour(06).withMinute(00).withSecond(00);
				}

				// 야간 근로 시간
				Double nightWorks = (double) Duration.between(start, end).toMinutes() / 60;

				this.saveAllowance(salaryPayment.getPaymentId(), "야간근로수당",
						avgSalary, nightWorks, totalPayment, allowanceList);
			}
		}

		return allowanceList;
	}

	// 근로 수당 목록에 추가
	private void saveAllowance(Long paymentId, String allowanceName, Long avgSalary,
			double workHours, AtomicLong totalPayment, List<Allowance> allowanceList) {
		Allowance allowance = new Allowance();

		allowance.setPaymentId(paymentId);
		allowance.setAllowanceName(allowanceName);
		allowance.setAllowanceAmount((long) Math.ceil(avgSalary * workHours * 0.5));

		if (allowance.getAllowanceAmount() != 0) {
			totalPayment.set(totalPayment.get() + allowance.getAllowanceAmount());
			allowanceList.add(allowance);
		}
	}

	// 연차 수당 (폐기)

	// 공제 목록
	private List<Deduction> getDeductionList(Long paymentId, AtomicLong totalPayment) {
		List<Deduction> deductionList = new ArrayList<>();
		String[] categories = {"국민연금", "건강보험", "고용보험"};

		for (String c : categories) {
			Deduction deduction = new Deduction();
			deduction.setPaymentId(paymentId);
			deduction.setDeductionName(c);

			Double percent = switch (c) {
				case "국민연금":
					yield nationalPension;
				case "건강보험":
					yield healthInsurance;
				case "고용보험":
					yield employmentInsurance;
				default:
					throw new IllegalStateException("Unexpected value: " + c);
			};
			Long amount = (long) Math.ceil((double) totalPayment.get() * percent / 100);
			deduction.setDeductionAmount(amount);

			if (deduction.getDeductionAmount() != 0) {
				deductionList.add(deduction);
			}

			// 장기요양보험: 건강보험 중 n%
			if ("건강보험".equals(c)) {
				Deduction deduction2 = new Deduction();
				deduction2.setPaymentId(paymentId);
				deduction2.setDeductionName("장기요양보험");

				Long amount2 = (long) Math.ceil((double) deduction.getDeductionAmount() * nursingInsurance / 100);
				deduction2.setDeductionAmount(amount2);

				if (deduction.getDeductionAmount() != 0) {
					deductionList.add(deduction2);
				}
			}
		}

		return deductionList;
	}
}
