package com.goodee.corpdesk.email;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SendDTO {

	private String to;
	private String from;
	private String subject;
	private String text;
	private String user;
}
