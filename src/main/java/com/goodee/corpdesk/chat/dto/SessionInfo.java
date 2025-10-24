package com.goodee.corpdesk.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SessionInfo {
	private Long chatRoomId;
	private String username;
	private boolean focused;
	
	public SessionInfo(Long chatRoomId, String username) {
		this.chatRoomId=chatRoomId;
		this.username=username;
	}
}
