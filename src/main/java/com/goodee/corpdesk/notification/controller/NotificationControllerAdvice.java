package com.goodee.corpdesk.notification.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import com.goodee.corpdesk.chat.dto.ChatMessageDto;
import com.goodee.corpdesk.chat.service.ChatRoomService;
import com.goodee.corpdesk.notification.dto.NotificationDto;
import com.goodee.corpdesk.notification.service.NotificationService;


@ControllerAdvice
public class NotificationControllerAdvice {
	@Autowired
	ChatRoomService chatRoomService;
	@Autowired
	NotificationService notificationService;
	
	//알림은 모든 페이지 에서 공통으로 봐야함으로
	@ModelAttribute
	public void messageNotification(Model model , Principal principal) {
		if(principal==null) {
			return;
		}
		List<ChatMessageDto> msgNotificationList=  chatRoomService.getChatNotificationList(principal.getName());
		List<NotificationDto> approvalNotificationList = notificationService.getApprovalNotificationList(principal.getName());
		model.addAttribute("msgNotificationList",msgNotificationList);
		model.addAttribute("approvalNotificationList",approvalNotificationList);
	}
}
