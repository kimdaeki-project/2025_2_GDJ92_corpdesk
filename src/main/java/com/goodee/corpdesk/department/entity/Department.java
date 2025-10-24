package com.goodee.corpdesk.department.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import com.goodee.corpdesk.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@ToString
@SuperBuilder
@Builder @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "department")
@DynamicInsert
@DynamicUpdate
public class Department extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer departmentId;

    private Integer parentDepartmentId;

    @Column(nullable = false)
    private String departmentName;

    public ResApprovalDTO toResApprovalDTO() {
        return ResApprovalDTO.builder()
                .departmentId(departmentId)
                .departmentName(departmentName)
                .build();
    }

    // departmentName만 받는 생성자 추가
    public Department(Integer parentDepartmentId, String departmentName) {
        this.parentDepartmentId = parentDepartmentId;
        this.departmentName = departmentName;
    }

}
