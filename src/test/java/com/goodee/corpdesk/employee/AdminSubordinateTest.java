package com.goodee.corpdesk.employee;

import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.department.repository.DepartmentRepository;
import com.goodee.corpdesk.position.entity.Position;
import com.goodee.corpdesk.position.repository.PositionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AdminSubordinateTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmployeeService employeeService;

    private List<Employee> admins;

    @BeforeEach
    void setUp() {
        List<String> adminUsernames = List.of("admin001", "admin002", "admin003", "admin004", "admin005");
        admins = employeeRepository.findAllById(adminUsernames);
    }

    @Test
    @DisplayName("각 관리자에게 3명의 부하직원을 추가하는 테스트")
    void add_subordinates_to_admins_test() throws Exception {
        // Given
        List<Employee> newSubordinates = new ArrayList<>();
        int subordinateCounter = 1;

        // When
        for (Employee admin : admins) {
            // 1. 관리자의 하위 직급 찾기
            Optional<Position> subordinatePositions = positionRepository.findByParentPositionIdAndUseYnTrue(admin.getPositionId());
            // 하위 직급이 없으면 그 다음 직급으로 설정 (테스트 데이터 기반)
            Integer subordinatePositionId = subordinatePositions.isEmpty()
                    ? admin.getPositionId() + 1
                    : subordinatePositions.get().getPositionId();

            for (int i = 1; i <= 3; i++) {
                String subordinateUsername = "user" + subordinateCounter++;
                Employee subordinate = Employee.builder()
                        .username(subordinateUsername)
                        .password("pass123")
                        .name("부하직원" + (subordinateCounter - 1))
                        .departmentId(admin.getDepartmentId())
                        .positionId(subordinatePositionId)
                        .hireDate(LocalDate.now())
                        .mobilePhone("010-0000-" + String.format("%04d", subordinateCounter - 1)) // Ensure uniqueness
                        .roleId(3) // ROLE_USER
                        .build();
                newSubordinates.add(subordinate);
            }
        }

        for(Employee e : newSubordinates) {
            employeeService.addEmployee(e);
        }

        // Then
        for (Employee admin : admins) {
            Optional<Position> subordinatePositions = positionRepository.findByParentPositionIdAndUseYnTrue(admin.getPositionId());
            Integer subordinatePositionId = subordinatePositions.isEmpty()
                    ? admin.getPositionId() + 1
                    : subordinatePositions.get().getPositionId();

            List<Employee> foundSubordinates = employeeRepository.findByDepartmentIdAndPositionId(admin.getDepartmentId(), subordinatePositionId);

            // 새로 추가된 3명의 부하직원만 필터링
            long count = foundSubordinates.stream()
                                          .filter(e -> e.getUsername().startsWith("user"))
                                          .count();

            assertEquals(3, count, admin.getName() + " 관리자의 부하직원이 3명이어야 합니다.");
        }
    }

    // EmployeeRepository에 findByDepartmentIdAndPositionId 메서드가 없으므로 추가 필요
    // 임시로 EmployeeRepository 인터페이스를 수정하는 대신, 여기서 간단한 조회를 수행합니다.
    // 실제 프로젝트에서는 Repository에 메서드를 추가하는 것이 좋습니다.
    @Test
    @DisplayName("레포지토리 쿼리 테스트용")
    void query_test() {
        // 이 테스트는 실제 로직과 무관하며, 레포지토리 메서드 추가를 위한 제안입니다.
        // EmployeeRepository에 다음 메서드를 추가해야 합니다:
        // List<Employee> findByDepartmentIdAndPositionId(Integer departmentId, Integer positionId);
        
        // 예시:
        Integer departmentId = 11; // 인사팀
        Integer positionId = 8; // 대리
        List<Employee> employees = employeeRepository.findByDepartmentId(departmentId)
                .stream()
                .filter(e -> e.getPositionId().equals(positionId))
                .toList();
        
        assertNotNull(employees);
    }
}
