package com.goodee.corpdesk.employee;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@NoArgsConstructor
@Getter
@Setter
@Entity @Table
@DynamicInsert
public class Role {

    @Id
    private Integer roleId;
    private String roleName;
//    @CurrentTimestamp
//    private LocalDateTime createdAt;
//    @UpdateTimestamp
//    private LocalDateTime updatedAt;
//    private String modifiedBy;
//    @ColumnDefault("1")
//    private Boolean useYn;
}
