package com.goodee.corpdesk.notification.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import com.goodee.corpdesk.approval.service.ApprovalFormService;
import com.goodee.corpdesk.approval.service.ApprovalService;
import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeService;
import com.goodee.corpdesk.notification.dto.NotificationDto;
import com.goodee.corpdesk.notification.entity.Notification;
import com.goodee.corpdesk.notification.repository.NotificationRepository;

@Service
public class NotificationService {
	
	@Autowired
	private NotificationRepository notificationRepository;
	@Autowired
	private ApprovalFormService approvalFormService;
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired
	private EmployeeService employeeService;

  
	//읽지 않은 '메세지' 알림 조회
	public List<NotificationDto> getMessageNotification(String username) {
		List<Notification> notification =  notificationRepository.findByNotificationTypeAndUsernameAndIsReadFalseOrderByNotificationIdDesc("message",username);
		List<NotificationDto> notificationDto=new ArrayList<>();
		if(!notification.isEmpty()) {
			 notification.forEach(l->{
				 NotificationDto n= l.ChangeToDto();
				 notificationDto.add(n);
			 });
		}
		
		
		return notificationDto;
	}
	// 읽지 않은 '결재' 알림 조회
	public List<NotificationDto> getApprovalNotificationList(String username) {
		List<Notification> notification =  notificationRepository.findByNotificationTypeAndUsernameAndIsReadFalseOrderByNotificationIdDesc("approval",username);
		List<NotificationDto> notificationDto=new ArrayList<>();
		if(!notification.isEmpty()) {
			 notification.forEach(l->{
				 System.out.println(l.getRelatedId());
				 NotificationDto n= l.ChangeToDto();
				 notificationDto.add(n);
			 });
		}
		return notificationDto;
	}
	
	
	//특정 알림 유형의 모든 알림 읽음 처리
	public int setNotificationReadAll(String notificationType , String username) {
		int result= notificationRepository.updateAllReadTrue(notificationType,username);
		return result;
	
	}
	//채팅방 하나의 메세지 전부 읽음 처리
	public int setNotificationOneRoomReadAll(Long chatRoomId , String username) {
		int result= notificationRepository.updateOneRoomAllReadTrue(chatRoomId,username);
		return result;
	
	}
	//메세지 알림 저장
	public Notification saveNotification(Long relatedId ,String notificationType , String username) {
		Notification notification = Notification.builder()
				.notificationType(notificationType)
				.relatedId(relatedId)
				.username(username)
				.build();
		return notificationRepository.save(notification);
	}
	//결재 알림 저장
	public Notification saveApprovalNotification(Long relatedId ,Integer formId,String drafterName,String notificationType ,String content, String username) throws Exception {
		ResApprovalDTO res= approvalFormService.getApprovalForm(formId);
		Map<String, Object> map = employeeService.detail(drafterName);
        Employee emp = (Employee) map.get("employee");
        
		res.getFormTitle();
		Notification notification = Notification.builder()
				.notificationType(notificationType)
				.relatedId(relatedId)
				.username(username)
				.content(content)
				.title(res.getFormTitle()+"_"+emp.getName())
				.build();
		return notificationRepository.save(notification);
	}
	//결재 알림 읽음
	public NotificationDto readApprovalNotification(Long relatedId ,String notificationType , String username) {
		List<Notification> notification =  notificationRepository.findByRelatedIdAndNotificationTypeAndUsername(relatedId,notificationType,username);
		if(notification.size()>0) {
			notificationRepository.updateReadTrue(relatedId,notificationType,username);
		}
		return null;
		}
	
	
	
	
	//결재 알림 요청
	public void reqNotification(Long relatedId ,Integer formId,String drafterName ,String notificationType , String content , String username ) throws Exception {
		Notification n = saveApprovalNotification(relatedId,formId,drafterName,
				notificationType, content,username);
		messagingTemplate.convertAndSendToUser(username, "/queue/notifications",n );
	}

	
}
