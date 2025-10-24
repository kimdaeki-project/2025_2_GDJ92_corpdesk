package com.goodee.corpdesk.employee;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import com.goodee.corpdesk.employee.dto.EmployeeSecurityDTO;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

    @NativeQuery("""
        WITH e AS (
        	SELECT *
        	FROM employee
        	WHERE
                use_yn = :useYn
                AND username = :username
        )
        SELECT
            e.username AS username, e.position_id AS position_id, e.department_id AS department_id, e.name AS name
            , d.department_name AS department_name, d.parent_department_id AS parent_department_id
            , p.position_name AS position_name, p.parent_position_id AS parent_position_id
        FROM e
        LEFT JOIN department d USING (department_id)
        LEFT JOIN `position` p USING (position_id);
    """)
    public ResEmployeeDTO findEmployeeWithDeptAndPosition(@Param("useYn") Boolean useYn
                                                          , @Param("username") String username);

    @NativeQuery("""
        WITH
        	d AS (
        		SELECT department_id, department_name
        		FROM department
        		WHERE department_id = :departmentId
        ),
        	e AS (
        		SELECT *
        		FROM employee
        		WHERE use_yn = :useYn
        )
        SELECT
        	d.department_name AS department_name
        	, e.username AS username, e.name AS name
        	, p.position_name AS position_name
        	, ef.file_id AS file_id, ef.ori_name AS ori_name, ef.save_name AS save_name
        	, ef.extension AS extension
        FROM d
        JOIN e USING (department_id)
        JOIN `position` p USING (position_id)
        LEFT JOIN employee_file ef USING (username)
    """)
    public List<ResApprovalDTO> findEmployeeWithDeptAndPositionAndFile(@Param("departmentId") Integer departmentId, @Param("useYn") Boolean useYn);

    @Query("""
        SELECT e
        FROM Employee e
        WHERE
            e.useYn = :useYn
        	AND MONTH(e.hireDate) = :month
        	AND DAY(e.hireDate) = :day
    """)
    public List<Employee> findAllByHireDateMonthDay(@Param("useYn") Boolean useYn, @Param("month") Integer month, @Param("day") Integer day);

	boolean existsByUsername(String username);

	List<Employee> findAllByUseYnTrue();

	boolean existsByMobilePhone(String mobilePhone);

	Optional<Employee> findByUsername(String username);
	
	List<Employee> findByDepartmentId(Integer departmentId);
	
	@Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.departmentId = null WHERE e.departmentId = :deptId")
    void clearDepartmentByDeptId(@Param("deptId") Integer deptId);

	List<Employee> findByDepartmentIdAndUseYnTrue(Integer departmentId);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("update Employee e set e.departmentId = :newDeptId where e.departmentId = :oldDeptId")
	int moveAllByDepartment(@Param("oldDeptId") Integer oldDeptId,
	                        @Param("newDeptId") Integer newDeptId);


	@Query("""
	SELECT new com.goodee.corpdesk.employee.EmployeeInfoDTO (
		e.username,
		e.name,
		e.hireDate,
		d.departmentName,
		p.positionName
	)
	FROM Employee e
	LEFT JOIN Department d ON e.departmentId = d.departmentId
	LEFT JOIN Position p ON e.positionId = p.positionId
	WHERE e.username = :username
""")
	Optional<EmployeeInfoDTO> findByIdWithDept(String username);


	@Query(value = """
	SELECT
	    e.username AS username,
	    e.name AS name,
	    IFNULL(d.department_name, '-') AS departmentName,
	    IFNULL(p.position_name, '-') AS positionName,
	    r.role_name AS roleName
	FROM employee e
	LEFT JOIN department d ON e.department_id = d.department_id
	LEFT JOIN position p ON p.position_id = e.position_id
	LEFT JOIN role r ON e.role_id = r.role_id
	WHERE e.use_yn = 1
""",
			countQuery = "SELECT COUNT(*) FROM employee WHERE use_yn = 1",
	nativeQuery = true)
	Page<Map<String, Object>> findAllWithDepartmentAndPosition(Pageable pageable);

	@Query("""
	SELECT new com.goodee.corpdesk.employee.dto.EmployeeSecurityDTO (
		e.username,
		e.password,
		e.name,
		
		e.accountNonExpired,
		e.accountNonLocked,
		e.credentialsNonExpired,
		e.enabled,
		
		e.updatedAt,
		e.createdAt,
		e.modifiedBy,
		e.useYn,
		
		e.roleId,
		r.roleName
	)
	FROM Employee e
	LEFT JOIN Role r ON e.roleId = r.roleId
	WHERE e.username = :username
""")
	Optional<EmployeeSecurityDTO> findEmployeeSecurityByUsername(@Param("username") String username);
	
	List<Employee> findByDepartmentIdAndPositionId(Integer departmentId, Integer positionId);
		
	Optional<EmailOnly> findExternalEmailByUsername(String username);
	
	boolean existsByMobilePhoneAndUsernameNot(String mobilePhone, String username);
	
}
