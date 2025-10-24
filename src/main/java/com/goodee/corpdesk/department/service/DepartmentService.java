package com.goodee.corpdesk.department.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import com.goodee.corpdesk.department.dto.DepartmentDetailDTO;
import com.goodee.corpdesk.department.dto.DepartmentTreeDTO;
import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.department.repository.DepartmentRepository;
import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeRepository;
import com.goodee.corpdesk.position.repository.PositionRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class DepartmentService {

    @Autowired
	private DepartmentRepository departmentRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private PositionRepository positionRepository;

    public List<ResApprovalDTO> getApprovalFormList() throws Exception {
        List<Department> result = departmentRepository.findAll();

        return result.stream().map(Department::toResApprovalDTO).toList();
    }

    public ResApprovalDTO getDepartment(String username) throws Exception {
        // 1. 유저의 부서 정보 얻어오기
        Optional<Employee> result = employeeRepository.findById(username);
        Employee employee = result.get();

        // 2. 부서 정보의 부서명 얻어오기
        Optional<Department> result2 = departmentRepository.findById(employee.getDepartmentId());
        Department department = result2.get();

        // 3. DTO에 담기
        ResApprovalDTO resApprovalDTO = new ResApprovalDTO();
        resApprovalDTO.setDepartmentId(department.getDepartmentId());
        resApprovalDTO.setDepartmentName(department.getDepartmentName());
        resApprovalDTO.setParentDepartmentId(department.getParentDepartmentId());

        return resApprovalDTO;
    }

    public  ResApprovalDTO getDepartment(Integer departmentId) throws Exception {
        // 2. 부서 정보의 부서명 얻어오기
        Optional<Department> result = departmentRepository.findById(departmentId);
        Department department = result.get();

        // 3. DTO에 담기
        ResApprovalDTO resApprovalDTO = new ResApprovalDTO();
        resApprovalDTO.setDepartmentId(department.getDepartmentId());
        resApprovalDTO.setDepartmentName(department.getDepartmentName());
        resApprovalDTO.setParentDepartmentId(department.getParentDepartmentId());

        return resApprovalDTO;
    }

    public Integer getDepartmentIdByName(String departmentName) {
        Optional<Department> department = departmentRepository.findByDepartmentName(departmentName);
        return department.map(Department::getDepartmentId).orElse(null);
    }

    public List<DepartmentTreeDTO> getDepartmentTree() {
        List<Department> departments = departmentRepository.findByUseYnTrue();
        List<DepartmentTreeDTO> tree = new ArrayList<>();

        for (Department dept : departments) {
            // parentDepartmentId 사용
            String parent = (dept.getParentDepartmentId() == null) ? "#" : dept.getParentDepartmentId().toString();
            tree.add(new DepartmentTreeDTO(dept.getDepartmentId(), dept.getDepartmentName(), parent));
        }

        return tree;
    }



    public DepartmentDetailDTO getDepartmentDetail(Integer id) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("부서 없음"));

        // 상위 부서명
        String parentName = null;
        if (dept.getParentDepartmentId() != null) {
            parentName = departmentRepository.findById(dept.getParentDepartmentId())
                    .map(Department::getDepartmentName)
                    .orElse(null);
        }

        // 하위 부서들
        List<String> childDepartments = departmentRepository.findByParentDepartmentIdAndUseYnTrue(dept.getDepartmentId())
                .stream()
                .map(Department::getDepartmentName)
                .toList();

        // 직원 목록 (PositionRepository 통해 직위명 가져오기)
        List<DepartmentDetailDTO.MemberDTO> members = employeeRepository.findByDepartmentIdAndUseYnTrue(dept.getDepartmentId())
        	    .stream()
        	    .map(emp -> new DepartmentDetailDTO.MemberDTO(
        	        emp.getUsername(), 
        	        emp.getName(),
        	        positionRepository.findById(emp.getPositionId())
        	                          .map(p -> p.getPositionName())
        	                          .orElse("정보 없음")
        	    ))
        	    .toList();

        return DepartmentDetailDTO.builder()
                .departmentId(dept.getDepartmentId())
                .departmentName(dept.getDepartmentName())
                .employeeCount(members.size())
                .createdDate(
                    dept.getCreatedAt() != null
                        ? dept.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                        : "정보 없음"
                )
                .parentDepartmentName(parentName != null ? parentName : "미지정")
                .childDepartments(
                    childDepartments.isEmpty() ? List.of("정보 없음") : childDepartments
                )
                .members(members)
                .build();
    }

    @Transactional
    public void moveEmployees(List<String> usernames, Integer newDeptId) {
        for (String username : usernames) {
            Employee emp = employeeRepository.findById(username)
                    .orElseThrow(() -> new RuntimeException("직원 없음: " + username));
            emp.setDepartmentId(newDeptId);
            employeeRepository.save(emp);
        }
    }

    @Transactional
    public void deactivateDepartment(Integer id) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("부서를 찾을 수 없습니다."));

        Integer parentId = dept.getParentDepartmentId();
        boolean isRoot = (parentId == null);

        // 1) 부서원 이동
        if (isRoot) {
            // 루트면: 부서원 departmentId 를 null 로 (스키마가 NULL 허용)
            employeeRepository.clearDepartmentByDeptId(id);
            // 만약 NULL 불가 스키마라면, 여기서 '미배정' 부서로 이동하도록 변경하세요.
            // employeeRepository.moveAllByDepartment(id, unassignedDeptId);
        } else {
            // 루트가 아니면 상위 부서로 이동
            employeeRepository.moveAllByDepartment(id, parentId);
        }

        // 2) 하위 부서 재부모 지정
        if (isRoot) {
            // 루트면: 자식들을 최상위로 승격 (parentDepartmentId = null)
            departmentRepository.reparentChildrenToNull(id);
        } else {
            // 루트가 아니면: 자식들을 현재 부서의 상위로 올림
            departmentRepository.reparentChildren(id, parentId);
        }

        // 3) 현재 부서 비활성화(Soft delete)
        dept.setUseYn(false);
        departmentRepository.save(dept);
    }
    
    @Transactional
    public void excludeEmployees(List<String> usernames) {
        for (String username : usernames) {
            Employee emp = employeeRepository.findById(username)
                    .orElseThrow(() -> new RuntimeException("직원 없음: " + username));
            emp.setDepartmentId(null);      // 부서 ID null 처리
            employeeRepository.save(emp);
        }
    }
    
    public Department addOrReactivateDepartment(String rawName, Integer parentId) {
        String name = rawName == null ? "" : rawName.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("부서명을 입력하세요.");
        }

        // 1) 부모 유효성(선택적으로 강화)
        if (parentId != null) {
            departmentRepository.findByDepartmentIdAndUseYnTrue(parentId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상위 부서입니다."));
        }

        // 2) 활성 중복 체크
        if (departmentRepository.existsByDepartmentNameAndUseYnTrue(name)) {
            throw new IllegalStateException("이미 존재하는 부서명입니다.");
        }

        // 3) 비활성 동명 재활성화
        Optional<Department> inactive = departmentRepository.findByDepartmentNameAndUseYnFalse(name);
        if (inactive.isPresent()) {
            Department d = inactive.get();

            if (parentId != null && parentId.equals(d.getDepartmentId())) {
                throw new IllegalArgumentException("자기 자신을 상위 부서로 지정할 수 없습니다.");
            }

            d.setUseYn(true);
            d.setParentDepartmentId(parentId);
            return departmentRepository.save(d);
        }

        // 4) 신규 생성
        Department d = new Department();
        d.setDepartmentName(name);
        d.setParentDepartmentId(parentId);
        d.setUseYn(true);
        return departmentRepository.save(d);
    }
    
    
}
