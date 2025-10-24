package com.goodee.corpdesk.holiday.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HolidayHeaderDTO {

    private String resultCode;
    private String resultMsg;

}
