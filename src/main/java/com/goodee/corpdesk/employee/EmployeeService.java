package com.goodee.corpdesk.employee;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import com.goodee.corpdesk.attendance.DTO.ResAttendanceDTO;
import com.goodee.corpdesk.attendance.entity.Attendance;
import com.goodee.corpdesk.attendance.service.AttendanceService;
import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.department.repository.DepartmentRepository;
import com.goodee.corpdesk.email.EmailService;
import com.goodee.corpdesk.email.SendDTO;
import com.goodee.corpdesk.employee.dto.EmployeeListDTO;
import com.goodee.corpdesk.employee.dto.EmployeeSecurityDTO;
import com.goodee.corpdesk.file.FileManager;
import com.goodee.corpdesk.file.dto.FileDTO;
import com.goodee.corpdesk.file.entity.EmployeeFile;
import com.goodee.corpdesk.file.repository.EmployeeFileRepository;
import com.goodee.corpdesk.position.entity.Position;
import com.goodee.corpdesk.position.repository.PositionRepository;
import com.goodee.corpdesk.salary.dto.EmployeeSalaryDTO;
import com.goodee.corpdesk.salary.entity.Allowance;
import com.goodee.corpdesk.salary.entity.Deduction;
import com.goodee.corpdesk.salary.entity.SalaryPayment;
import com.goodee.corpdesk.salary.repository.AllowanceRepository;
import com.goodee.corpdesk.salary.repository.DeductionRepository;
import com.goodee.corpdesk.salary.repository.SalaryRepository;
import com.goodee.corpdesk.vacation.VacationManager;
import com.goodee.corpdesk.vacation.entity.Vacation;
import com.goodee.corpdesk.vacation.repository.VacationRepository;


@Transactional
@Service
public class EmployeeService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AesBytesEncryptor aesBytesEncryptor;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private PositionRepository positionRepository;
	@Autowired
	private SalaryRepository salaryRepository;
	@Autowired
	private AllowanceRepository allowanceRepository;
	@Autowired
	private DeductionRepository deductionRepository;

    @Autowired
    private RoleService roleService;
    @Autowired
    private EmployeeFileRepository employeeFileRepository;
    @Autowired
    private FileManager fileManager;
    @Value("${app.upload}")
    private String uploadPath;

    
    
    @Autowired
    private VacationRepository vacationRepository;
    @Autowired
    private VacationManager vacationManager;

	@Autowired
	private EmailService emailService;

    // ---------------------- Controller용 Service 메서드 ----------------------

    // 모든 부서 조회
    public List<Department> getAllDepartments() {
    	return departmentRepository.findByUseYnTrue();
    }

    // 모든 직위 조회
    public List<Position> getAllPositions() {
        return positionRepository.findByUseYnTrueOrderByPositionIdAsc();
    }

    // username 존재 여부 체크
    public boolean isUsernameExists(String username) {
        return employeeRepository.existsByUsername(username);
    }

    // 휴대폰 존재 여부 체크
    public boolean isMobilePhoneExists(String mobilePhone) {
    	String normalized = canonMobile(mobilePhone);
    	return normalized != null && employeeRepository.existsByMobilePhone(normalized);
    }

    // username으로 Employee 조회 (없으면 예외)
    public Employee getEmployeeOrThrow(String username) {
        return employeeRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("직원을 찾을 수 없습니다: " + username));
    }

    // 직원 출퇴근 내역 가져오기
    public List<Attendance> getAttendanceByUsername(String username) {
        return attendanceService.getAttendanceByUsername(username);
    }

    // 직원 프로필 파일 조회
    public Optional<EmployeeFile> getEmployeeFileByUsername(String username) {
        return employeeFileRepository.findByUseYnAndUsername(true, username);
    }

 // 직원 등록
    public Employee addEmployee(Employee employee) throws Exception {
    	// 0) username 필수 및 중복 금지
    	String username = canon(employee.getUsername());
    	if (username == null || username.isBlank()) {
    	  throw new IllegalArgumentException("username는 필수입니다.");
    	   }  
    	   employee.setUsername(username);
    	   // ✅ 휴대폰 정규화 + 신규 중복 검사
    	    String normalized = canonMobile(employee.getMobilePhone());
    	    if (normalized != null) {
    	        if (employeeRepository.existsByMobilePhone(normalized)) {
    	            throw new IllegalStateException("이미 사용 중인 휴대폰 번호입니다: " + employee.getMobilePhone());
    	        }
    	    }
    	    employee.setMobilePhone(normalized);
    	   
    	  if (employeeRepository.existsById(username)) {
    	    throw new IllegalStateException("이미 존재하는 직원입니다: " + username);
    	   }

        // 1) 비밀번호 기본값 보장 (엑셀에서 비번 열 제거했을 때 대비)
        String raw = employee.getPassword();
        if (raw == null || raw.isBlank()) {
            raw = "1234"; // 정책에 맞게 기본 비번 지정 (원하면 랜덤 생성으로 대체)
        }
        employee.setPassword(passwordEncoder.encode(raw));

        // 2) 공통 생성 로직
        employee.setUseYn(true);
        roleService.assignRole(employee);
        Employee newEmployee = employeeRepository.save(employee);

        // 3) 휴가 테이블 초기화
        Vacation newVacation = new Vacation();
        newVacation.setUsername(employee.getUsername());

        int totalVacation = 0;
        LocalDate hireDate = employee.getHireDate();
        if (hireDate != null) {
            totalVacation = vacationManager.calTotalVacation(hireDate);
        }
        newVacation.setTotalVacation(totalVacation);
        newVacation.setRemainingVacation(totalVacation);
        newVacation.setUsedVacation(0);

        vacationRepository.save(newVacation);

        return newEmployee;
    }
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<EmployeeListDTO> getActiveEmployeesForList() {
        List<Employee> employees = employeeRepository.findAllByUseYnTrue();
        List<EmployeeListDTO> result = new ArrayList<>();

        for (Employee emp : employees) {
            String deptName = "-";
            if (emp.getDepartmentId() != null) {
                deptName = departmentRepository.findByDepartmentIdAndUseYnTrue(emp.getDepartmentId())
                           .map(Department::getDepartmentName)
                           .orElse("-");
            }
            String posName = "-";
            if (emp.getPositionId() != null) {
                posName = positionRepository.findByPositionIdAndUseYnTrue(emp.getPositionId())
                           .map(Position::getPositionName)
                           .orElse("-");
            }

            String workStatus = "-";
            try {
                ResAttendanceDTO attendanceDto = attendanceService.getCurrentAttendance(emp.getUsername());
                if (attendanceDto != null &&
                   ("출근".equals(attendanceDto.getWorkStatus()) || "퇴근".equals(attendanceDto.getWorkStatus()))) {
                    workStatus = attendanceDto.getWorkStatus();
                }
            } catch (Exception e) { workStatus = "-"; }

            // ✅ 여기! 보기용으로 하이픈을 넣어 내려준다
            String formattedMobile = formatMobile(emp.getMobilePhone());

            result.add(new EmployeeListDTO(
            	    emp.getUsername(),
            	    emp.getName(),
            	    deptName,
            	    emp.getDepartmentId(),
            	    emp.getPositionId(),
            	    posName,
            	    emp.getMobilePhone(),      // DB 원본 (숫자만)
            	    formattedMobile,           // ✅ 표시용
            	    emp.getHireDate(),
            	    emp.getLastWorkingDay(),
            	    emp.getEnabled(),
            	    workStatus,
            	    emp.getPassword(),
            	    emp.getUseYn()
            	));
        }
        return result;
    }






 // 직원 정보 수정 (파일 포함)
    public void updateEmployee(Employee employeeFromForm, MultipartFile profileImageFile) throws Exception {
        Employee persisted = employeeRepository.findById(employeeFromForm.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee id: " + employeeFromForm.getUsername()));

    
        
        
        // Employee 필드 업데이트
        persisted.setName(employeeFromForm.getName());
        persisted.setEmployeeType(employeeFromForm.getEmployeeType());
        persisted.setExternalEmail(employeeFromForm.getExternalEmail());
        // ✅ 휴대폰 자기 자신 제외 중복 검사
        String normalizedMobile = canonMobile(employeeFromForm.getMobilePhone());
        if (normalizedMobile != null) {
            boolean takenByOthers = employeeRepository
                    .existsByMobilePhoneAndUsernameNot(normalizedMobile, persisted.getUsername());
            if (takenByOthers) {
                throw new IllegalStateException("이미 사용 중인 휴대폰 번호입니다: " + employeeFromForm.getMobilePhone());
            }
        }
        persisted.setMobilePhone(normalizedMobile);
        persisted.setDepartmentId(employeeFromForm.getDepartmentId());
        persisted.setPositionId(employeeFromForm.getPositionId());
        persisted.setHireDate(employeeFromForm.getHireDate());
        persisted.setAddress(employeeFromForm.getAddress());
        persisted.setBirthDate(employeeFromForm.getBirthDate());
        persisted.setEnglishName(employeeFromForm.getEnglishName());
        persisted.setVisaStatus(employeeFromForm.getVisaStatus());
        persisted.setResidentNumber(employeeFromForm.getResidentNumber());
        persisted.setNationality(employeeFromForm.getNationality());
        
        // ---------------- 비밀번호 처리 ----------------
        if (employeeFromForm.getPassword() != null && !employeeFromForm.getPassword().isEmpty()) {
            persisted.setPassword(passwordEncoder.encode(employeeFromForm.getPassword()));
        } // null이면 기존 비밀번호 그대로 유지

        if (employeeFromForm.getEnabled() != null) {
            persisted.setEnabled(employeeFromForm.getEnabled());
        }
        persisted.setGender(employeeFromForm.getGender());
        persisted.setLastWorkingDay(employeeFromForm.getLastWorkingDay());
        persisted.setDirectPhone(employeeFromForm.getDirectPhone());

        Employee editedEmployee = employeeRepository.save(persisted);
        
        roleService.assignRole(editedEmployee);
        employeeRepository.save(editedEmployee);
        // ---------------- 프로필 이미지 처리 ----------------
        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            Optional<EmployeeFile> fileOptional = employeeFileRepository.findByUsername(persisted.getUsername());
            String profileImagePath = uploadPath + "profile" + File.separator;
            FileDTO fileDTO = fileManager.saveFile(profileImagePath, profileImageFile);

            if (fileOptional.isPresent()) {
                EmployeeFile existingFile = fileOptional.get();
                existingFile.setOriName(fileDTO.getOriName());
                existingFile.setSaveName(fileDTO.getSaveName());
                existingFile.setExtension(fileDTO.getExtension());
                existingFile.setUseYn(true);
                employeeFileRepository.save(existingFile);
            } else {
                EmployeeFile newFile = new EmployeeFile();
                newFile.setUsername(persisted.getUsername());
                newFile.setOriName(fileDTO.getOriName());
                newFile.setSaveName(fileDTO.getSaveName());
                newFile.setExtension(fileDTO.getExtension());
                newFile.setUseYn(true);
                employeeFileRepository.save(newFile);
            }
        }

        // 추가) 직원 정보 수정 성공시 휴가 테이블 update
        if(editedEmployee.getHireDate() != null) {
            // 직원의 휴가 테이블 조회
//            Vacation vacation = vacationRepository.findByUseYnAndUsername(true, editedEmployee.getUsername());
//
//            // 총 연차 update
//            int totalVacation = vacationManager.calTotalVacation(editedEmployee.getHireDate());
//            vacation.setTotalVacation(totalVacation);
//            // 잔여 연차 update (update된 총 연차 - 사용 연차)
//            int usedVacation = vacation.getUsedVacation();
//            vacation.setRemainingVacation(totalVacation - usedVacation);
//
//            vacationRepository.save(vacation);
        }
    }


    // 프로필 이미지 삭제
    public void deleteProfileImage(String username) {
        Optional<EmployeeFile> fileOptional = employeeFileRepository.findByUsername(username);
        if (fileOptional.isPresent()) {
            EmployeeFile employeeFile = fileOptional.get();
            FileDTO fileDTO = new FileDTO(employeeFile.getSaveName(), employeeFile.getExtension());
            String profileImagePath = uploadPath + "profile" + File.separator;
            fileManager.deleteFile(profileImagePath, fileDTO);
            employeeFile.setUseYn(false);
            employeeFileRepository.save(employeeFile);
        }
    }


    // 단일 메서드로 통합
    @Transactional
    public void deactivateEmployee(String username, LocalDate lastWorkingDay) {
        Employee employee = employeeRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("해당 직원이 존재하지 않습니다."));
        
        // lastWorkingDay가 제공되면 설정
        if (lastWorkingDay != null) {
            employee.setLastWorkingDay(lastWorkingDay);
            employeeRepository.save(employee);
        }
        
        // 퇴사일자 검증
        if (employee.getLastWorkingDay() == null) {
            throw new IllegalStateException("퇴사일자를 먼저 설정해 주세요");
        }
        
        employee.setUseYn(false);
        employee.setEnabled(false);
        employeeRepository.save(employee);
    }
    

    // ---------------------- 기타 기존 메서드 ----------------------
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Employee employee = employeeRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        employee.setRole(roleRepository.findById(employee.getRoleId()).orElse(null));
		EmployeeSecurityDTO employee = employeeRepository.findEmployeeSecurityByUsername(username).get();
        return employee;
    }

    public Map<String, Object> detail(String username) {
        Employee employee = employeeRepository.findById(username).orElseThrow();
		Department department = null;
		if(employee.getDepartmentId() != null) department = departmentRepository.findByDepartmentIdAndUseYnTrue(employee.getDepartmentId()).orElse(null);
        Position position = null;
		if(employee.getPositionId() != null) position = positionRepository.findById(employee.getPositionId()).orElse(null);

        Map<String, Object> map = new HashMap<>();
        map.put("employee", employee);
        map.put("department", department);
        map.put("position", position);
        return map;
    }

	public Page<EmployeeSalaryDTO> getSalaryList(String username, Pageable pageable) {
		return salaryRepository.findAllEmployeeSalaryByUsername(username, pageable);
	}

	public Map<String, Object> getSalaryDetail(String username, Long paymentId) {
		Map<String, Object> map = new HashMap<>();
		SalaryPayment salaryPayment = salaryRepository.findByUsernameAndPaymentId(username, paymentId).get();

		if (salaryPayment == null) return null;

		List<Allowance> allowanceList = allowanceRepository.findAllByPaymentId(salaryPayment.getPaymentId());
		List<Deduction> deductionList = deductionRepository.findAllByPaymentId(salaryPayment.getPaymentId());

		EmployeeInfoDTO employee = employeeRepository.findByIdWithDept(salaryPayment.getUsername()).get();

		map.put("salaryPayment", salaryPayment);
		map.put("allowanceList", allowanceList);
		map.put("deductionList", deductionList);

		map.put("employee", employee);

		return map;
	}

	public Employee updatePassword(Employee employee, BindingResult bindingResult) {
		Optional<Employee> optional = employeeRepository.findById(employee.getUsername());
		Employee origin = optional.get();

		if (!passwordEncoder.matches(employee.getPassword(), origin.getPassword())) {
			bindingResult.addError(new FieldError("Employee", "password", "비밀번호가 다릅니다."));
			return null;
//			throw new BadCredentialsException("비밀번호가 다릅니다.");
		}

		if (!Objects.equals(employee.getPasswordNew(), employee.getPasswordCheck())) {
			bindingResult.addError(new FieldError("Employee", "passwordNew", "비밀번호 확인이 다릅니다."));
			return null;
//			throw new RuntimeException("비밀번호 확인이 다릅니다.");
		}

		String encoded = passwordEncoder.encode(employee.getPasswordNew());

		origin.setPassword(encoded);
		return origin;
//		return employeeRepository.save(origin);
	}

    public Employee detailSecret(String username) {
        Employee employee = employeeRepository.findById(username).orElseThrow();
        if (employee.getEncodedEmailPassword() != null) {
            byte[] decoded = aesBytesEncryptor.decrypt(employee.getEncodedEmailPassword());
            employee.setExternalEmailPassword(new String(decoded));
        }
        return employee;
    }

    public Employee updateEmail(Employee employee) {
        Employee origin = employeeRepository.findById(employee.getUsername()).orElseThrow();
        origin.setExternalEmail(employee.getExternalEmail());
        origin.setEncodedEmailPassword(aesBytesEncryptor.encrypt(employee.getExternalEmailPassword().getBytes()));
        return employeeRepository.save(origin);
    }

	public String getEmail(String username) {
		EmailOnly result = employeeRepository.findExternalEmailByUsername(username).get();

		return result.getExternalEmail();
	}

	public String resetPassword(String username) throws Exception {
		Employee employee = employeeRepository.findById(username).orElseThrow();

		SendDTO sendDTO = new SendDTO();
		sendDTO.setUser(username);
		sendDTO.setTo(employee.getExternalEmail());
		sendDTO.setSubject("Corpdesk 임시 비밀번호");

		String tempPassword = UUID.randomUUID().toString()
				.replace("-", "").substring(0, 10);

		String encoded = passwordEncoder.encode(tempPassword);
		employee.setPassword(encoded);

		sendDTO.setText("Corpdesk 임시 비밀번호: " + tempPassword);

		emailService.sendMail(sendDTO);

		return "success";
	}

    public ResEmployeeDTO getFulldetail(String username) {
        return employeeRepository.findEmployeeWithDeptAndPosition(true, username);
    }
    
    @Transactional
    public void updateFromImport(EmployeeListDTO dto) {
        Employee emp = employeeRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("직원 없음: " + dto.getUsername()));
        
        	// 휴대폰 중복 검사 (자신 제외)
	        String normalized = canonMobile(dto.getMobilePhone());
	        if (normalized != null && !normalized.isBlank()) {
	            boolean takenByOthers = employeeRepository
	                .existsByMobilePhoneAndUsernameNot(normalized, emp.getUsername());
	            if (takenByOthers) {
	                throw new IllegalStateException("이미 사용 중인 휴대폰 번호입니다: " + dto.getMobilePhone());
	            }
	        }
        
            // 부서 ID 유효성
            if (dto.getDepartmentId() != null) {
                departmentRepository.findByDepartmentIdAndUseYnTrue(dto.getDepartmentId())
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 부서 ID: " + dto.getDepartmentId()));
            }
        
            // 직위 ID 유효성
            if (dto.getPositionId() != null) {
                positionRepository.findByPositionIdAndUseYnTrue(dto.getPositionId())
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 직위 ID: " + dto.getPositionId()));
            }
        
            // 날짜 논리 검증
            if (dto.getHireDate() != null && dto.getLastWorkingDay() != null 
                    && dto.getLastWorkingDay().isBefore(dto.getHireDate())) {
                throw new IllegalArgumentException("퇴사일자가 입사일자보다 앞설 수 없습니다");
            }
        
        

        // 비밀번호/권한/파일 등은 손대지 않음
        if (dto.getName() != null) emp.setName(dto.getName());
        emp.setMobilePhone(normalized);        // 정책에 맞게 유효성/중복 검사를 호출부에서 수행
        emp.setDepartmentId(dto.getDepartmentId());      // 매칭 실패 시 null 가능
        emp.setPositionId(dto.getPositionId());
        emp.setHireDate(dto.getHireDate());
        emp.setLastWorkingDay(dto.getLastWorkingDay());

        employeeRepository.save(emp);
    }

    
 // 기존
    private static final Pattern ZERO_WIDTH =
        Pattern.compile("[\\u200B\\u200C\\u200D\\uFEFF]");

    // 개선: 숨은 문자 추가 (WORD JOINER, SOFT HYPHEN 등)
    private static final Pattern HIDDEN =
        Pattern.compile("[\\u200B\\u200C\\u200D\\uFEFF\\u2060\\u00AD]");

    // NBSP(00A0) 포함 모든 공백류를 일반 공백으로 통일 후 trim
    private static final Pattern ALL_SPACES =
        Pattern.compile("\\p{Z}+");

    // 제어문자 제거
    private static final Pattern CTRLS =
        Pattern.compile("\\p{Cntrl}+");

    private String canon(String s) {
        if (s == null) return "";
        String t = s;
        t = HIDDEN.matcher(t).replaceAll("");
        t = CTRLS.matcher(t).replaceAll("");          // 줄바꿈/탭 등 제어문자 제거
        t = ALL_SPACES.matcher(t).replaceAll(" ");    // 유니코드 공백 통일
        t = java.text.Normalizer.normalize(t, java.text.Normalizer.Form.NFKC);
        return t.strip();                              // 앞뒤 공백 제거
    }


 // 숫자만 남기기 (저장/중복체크용)
    private String canonMobile(String s) {
        if (s == null) return null;
        String t = canon(s);
        String onlyDigits = t.replaceAll("[^0-9]", "");
        return onlyDigits.isBlank() ? null : onlyDigits;  // ✅ 빈값은 null
    }

    // 보기용 하이픈 포맷 (목록/상세 출력용)
    private String formatMobile(String s) {
        if (s == null || s.isBlank()) return "-";
        String d = s.replaceAll("\\D", ""); // 혹시 저장이 정규화 안된 값이 있어도 방어
        if (d.isBlank()) return "-";

        // 대한민국 모바일 일반 규칙:
        // 11자리(예: 01012345678) -> 3-4-4
        // 10자리(예: 0111234567, 0161234567 등 구번호) -> 3-3-4
        if (d.length() == 11) {
            return d.replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
        } else if (d.length() == 10) {
            return d.replaceFirst("(\\d{3})(\\d{3})(\\d{4})", "$1-$2-$3");
        }
        
        return d;
    }

    
}


