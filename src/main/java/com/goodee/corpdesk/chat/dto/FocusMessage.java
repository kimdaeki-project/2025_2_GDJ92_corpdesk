package com.goodee.corpdesk.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FocusMessage {
	private Long chatRoomId;
    private boolean focused;
    private String notificationType;
}
