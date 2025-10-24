package com.goodee.corpdesk.chat.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.goodee.corpdesk.chat.service.ChatParticipantService;
import com.goodee.corpdesk.notification.service.NotificationService;


@Controller
@RequestMapping("/chat/participant/**")
public class ChatParticipantController {
	@Autowired
	ChatParticipantService chatParticipantService;
	@Autowired
	NotificationService notificationService;

    @Value("${cat.chat}")
    private String cat;

    @ModelAttribute("cat")
    public String getCat() {
        return cat;
    }

	@PostMapping("lastMessage/{roomId}")
	@ResponseBody
	public void saveLastMessage(@PathVariable(value="roomId") Long roomId,Principal principal) {
		String username = principal.getName();
		chatParticipantService.updateLastMessage(username,roomId);
		notificationService.setNotificationOneRoomReadAll(roomId,principal.getName());
	}
}
