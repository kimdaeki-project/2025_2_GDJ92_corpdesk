package com.goodee.corpdesk.employee;

import org.springframework.stereotype.Service;

import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.department.repository.DepartmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final DepartmentRepository departmentRepository;

    private boolean isHrDepartment(Integer deptId) {
        if (deptId == null) return false;

        Department dept = departmentRepository.findById(deptId).orElse(null);
        if (dept == null) return false;

        // 자기 자신이 인사팀이면 true
        if ("인사팀".equals(dept.getDepartmentName())) {
            return true;
        }

        // 상위 부서 확인 (재귀)
        return isHrDepartment(dept.getParentDepartmentId());
    }

    // 권한 부여 로직
    public void assignRole(Employee employee) {
        // 관리자(1번)는 그대로 유지
    	if ("admin".equalsIgnoreCase(employee.getUsername())) {
            employee.setRoleId(1);
            return;
        }

        if (isHrDepartment(employee.getDepartmentId())) {
            employee.setRoleId(2); // 인사팀 권한
        } else {
            employee.setRoleId(3); // 일반 직원 권한
        }
    }
}



