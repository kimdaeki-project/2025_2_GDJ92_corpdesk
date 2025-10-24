package com.goodee.corpdesk.employee.dto;

import java.time.LocalDate;

import com.goodee.corpdesk.employee.Employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeListDTO {
    private String username;
    private String name;
    private String departmentName;
    private Integer departmentId;
    private Integer positionId;
    private String positionName;
    private String mobilePhone;
    private String displayMobilePhone; // ✅ 표시용 (010-0000-0000 형태)
    private LocalDate hireDate;
    private LocalDate lastWorkingDay;
    private Boolean enabled;
    private String workStatus;  // 출근 / 퇴근 / -
    private String password;
    private boolean useYn;
    
    
 // EmployeeListDTO.java
    public Employee toEntity() {
        Employee e = new Employee();
        e.setUsername(this.username);
        e.setName(this.name);
        e.setDepartmentId(this.departmentId);
        e.setPositionId(this.positionId);
        e.setMobilePhone(this.mobilePhone);
        e.setHireDate(this.hireDate);
        e.setLastWorkingDay(this.lastWorkingDay);
        e.setPassword(this.password);
        e.setUseYn(this.useYn);
        return e;
    }

}
