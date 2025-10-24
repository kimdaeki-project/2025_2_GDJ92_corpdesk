package com.goodee.corpdesk.department.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goodee.corpdesk.department.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

	Optional<Department> findByDepartmentName(String departmentName);
	 Optional<Department> findByDepartmentId(Integer departmentId);
	List<Department> findByParentDepartmentId(Integer id);
	
	List<Department> findByUseYnTrue(); 
	List<Department> findByParentDepartmentIdAndUseYnTrue(Integer parentDepartmentId);
	// 부서 ID로 조회하되 활성 부서만
    Optional<Department> findByDepartmentIdAndUseYnTrue(Integer departmentId);
	
    
    // 자식들을 '최상위'(parentDepartmentId = null)로 승격
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Department d set d.parentDepartmentId = null where d.parentDepartmentId = :oldParentId")
    int reparentChildrenToNull(@Param("oldParentId") Integer oldParentId);

    // (이미 있다면 생략) 자식들을 지정한 새 부모로 재배치
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Department d set d.parentDepartmentId = :newParentId where d.parentDepartmentId = :oldParentId")
    int reparentChildren(@Param("oldParentId") Integer oldParentId, @Param("newParentId") Integer newParentId);
	
    boolean existsByDepartmentNameAndUseYnTrue(String departmentName);
    
    // 같은 이름의 비활성 부서 조회(재활성화 용)
    Optional<Department> findByDepartmentNameAndUseYnFalse(String departmentName);
}
