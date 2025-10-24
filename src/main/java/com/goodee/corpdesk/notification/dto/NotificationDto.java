package com.goodee.corpdesk.notification.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
	
	 private Long notificationId;
	    private String username; 
	    private String notificationType; 
	    private Long relatedId; 
	    private Boolean isRead;
	    private String content;
	    private LocalDateTime createdAt;
	    private String title;
}

