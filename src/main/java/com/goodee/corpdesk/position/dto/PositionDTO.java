package com.goodee.corpdesk.position.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PositionDTO {
	private Integer positionId;
    private String positionName;
    private Long employeeCount;
    private Integer parentPositionId;
}
