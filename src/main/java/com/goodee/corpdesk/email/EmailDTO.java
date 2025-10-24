package com.goodee.corpdesk.email;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmailDTO {

	private Integer emailNo;
	private String subject;
	private String text;
//	private Object content;
	private String from;
	private String sentDate;
	private String receivedDate;
//	private String to;
	private String recipients;

}
