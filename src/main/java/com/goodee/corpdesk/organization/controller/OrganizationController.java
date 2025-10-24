package com.goodee.corpdesk.organization.controller;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.goodee.corpdesk.department.dto.DepartmentDetailDTO;
import com.goodee.corpdesk.department.dto.MoveEmployeesDTO;
import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.department.repository.DepartmentRepository;
import com.goodee.corpdesk.department.service.DepartmentService;
import com.goodee.corpdesk.employee.EmployeeRepository;
import com.goodee.corpdesk.employee.RoleService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/organization") // 조직 관련 URL
@RequiredArgsConstructor
public class OrganizationController {

    private final DepartmentService departmentService;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleService roleService;

    @Value("${cat.organ}")
    private String cat;

    @ModelAttribute("cat")
    public String getCat() {
        return cat;
    }

    // 조직도 페이지
    @GetMapping("/list")
    public String listPage() {
        // /WEB-INF/views/organization/list.jsp
        return "organization/list";
    }

    
    @GetMapping("/tree")
    @ResponseBody
    public List<Map<String, Object>> getOrganizationTree() {
        List<Department> departments = departmentRepository.findByUseYnTrue(); // ✅ useYn=true만 가져오기

        List<Map<String, Object>> nodes = new ArrayList<>();
        for (Department dept : departments) {
            Map<String, Object> node = new HashMap<>();
            node.put("id", dept.getDepartmentId());
            node.put("parent", dept.getParentDepartmentId() == null ? "#" : dept.getParentDepartmentId());
            node.put("text", dept.getDepartmentName());
            nodes.add(node);
        }
        return nodes;
    }
    
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addDepartment(
            @RequestParam(value = "parentId", required = false) Integer parentId,
            @RequestParam("name") String name) {

        String trimmed = name == null ? "" : name.trim();
        if (trimmed.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "부서명을 입력하세요."));
        }

        try {
            Department dept = departmentService.addOrReactivateDepartment(trimmed, parentId);

            Map<String, Object> res = new HashMap<>();
            res.put("id", dept.getDepartmentId());
            res.put("parentId", dept.getParentDepartmentId());
            res.put("name", dept.getDepartmentName());
            return ResponseEntity.ok(res);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        }
    }




    
    
    @PostMapping("/deleteCascade")
    @ResponseBody
    public ResponseEntity<Void> deleteCascade(@RequestParam("id") Integer id) {
        departmentService.deactivateDepartment(id);
        return ResponseEntity.ok().build();
    }


    private void deleteWithChildren(Integer id) {
        List<Department> children = departmentRepository.findByParentDepartmentId(id);
        for(Department child : children) {
            deleteWithChildren(child.getDepartmentId());
        }
        departmentRepository.deleteById(id);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDetailDTO> getDepartmentDetail(@PathVariable("id") Integer id) {
        DepartmentDetailDTO dto = departmentService.getDepartmentDetail(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/moveEmployees")
    @ResponseBody
    public ResponseEntity<?> moveEmployees(@RequestBody MoveEmployeesDTO dto) {
        departmentService.moveEmployees(dto.getEmployeeUsernames(), dto.getNewDeptId());
        
     // 이동한 직원들 권한 재지정
        for (String username : dto.getEmployeeUsernames()) {
            employeeRepository.findById(username).ifPresent(emp -> {
                roleService.assignRole(emp);
                employeeRepository.save(emp);
            });
        }
        
        
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/excludeEmployees")
    @ResponseBody
    public ResponseEntity<?> excludeEmployees(@RequestBody MoveEmployeesDTO dto) {
        departmentService.excludeEmployees(dto.getEmployeeUsernames());
        return ResponseEntity.ok().build();
    }

    
   
}
