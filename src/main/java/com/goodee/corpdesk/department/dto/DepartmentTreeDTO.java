package com.goodee.corpdesk.department.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class DepartmentTreeDTO {
    private Integer id;       // JSTree에서 요구
    private String parent;   // JSTree에서 요구 ("#" = 루트)
    private String text;     // 부서 이름 표시
    
    
 // 생성자
    public DepartmentTreeDTO(Integer id, String text, String parent) {
        this.id = id;
        this.text = text;
        this.parent = parent;
    }
    
}



