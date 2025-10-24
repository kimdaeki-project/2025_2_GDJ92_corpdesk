package com.goodee.corpdesk.holiday.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class HolidayBodyDTO {

    private HolidayItemsDTO items;
    private Integer numOfRows;
    private Integer pageNo;
    private Integer totalCount;

}
