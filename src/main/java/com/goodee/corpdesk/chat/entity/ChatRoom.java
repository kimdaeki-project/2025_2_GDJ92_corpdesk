package com.goodee.corpdesk.chat.entity;

import java.time.LocalDateTime;

import com.goodee.corpdesk.chat.dto.RoomData;
import com.goodee.corpdesk.common.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long chatRoomId;
	private String chatRoomTitle;
	private String chatRoomType;
	
	@Transient
	private Long unreadCount;
	
	public RoomData changeToRoomData() {
		return RoomData.builder()
				.chatRoomId(chatRoomId)
				.chatRoomTitle(chatRoomTitle)
				.chatRoomType(chatRoomType)
				.build();	
		}
}

