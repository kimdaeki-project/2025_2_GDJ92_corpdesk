package com.goodee.corpdesk.notification.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.goodee.corpdesk.notification.service.NotificationService;

@Controller
@RequestMapping("/notification/**")
public class NotificationController {

	@Autowired
	private NotificationService notificationService;
	@PostMapping("read/{approvalId}")
	@ResponseBody
	public void readApproval(@PathVariable(value="approvalId") Long approvalId , Principal principal) {
		notificationService.readApprovalNotification(approvalId, "approval",principal.getName() );
		
	}
	@PostMapping("MsgReadAll")
	@ResponseBody
	public void msgReadAll(Principal principal) {
		notificationService.setNotificationReadAll("message", principal.getName());
	}
}
