package com.goodee.corpdesk.holiday.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class HolidayItemsDTO {

    @JacksonXmlElementWrapper(useWrapping = false)  // <items> 태그 내의 여러 <item>을 List로 처리
    private List<HolidayItemDTO> item;

}
