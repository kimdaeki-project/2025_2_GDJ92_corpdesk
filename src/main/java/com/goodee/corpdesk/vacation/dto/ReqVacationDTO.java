package com.goodee.corpdesk.vacation.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReqVacationDTO {

    private Integer vacationType;
    private String username;

    private String listType;

}
