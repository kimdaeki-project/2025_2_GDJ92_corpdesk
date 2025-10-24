package com.goodee.corpdesk.chat.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.goodee.corpdesk.chat.entity.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>{


	List<ChatMessage> findByChatRoomIdAndSentAtGreaterThanEqualOrderByMessageIdDesc(Long chatRoomId,LocalDateTime enterTime ,Pageable pageable);

	List<ChatMessage> findByChatRoomIdAndMessageIdLessThanAndSentAtGreaterThanEqualOrderByMessageIdDesc(Long chatRoomId, Long lastMessageNo,
			LocalDateTime enterTime,
			Pageable pageable);
	//시스템 메세지는 빼고
	ChatMessage findTopByChatRoomIdAndMessageTypeIsNullOrderByMessageIdDesc(Long chatRoomId);
	
	ChatMessage findTopByChatRoomIdOrderByMessageIdDesc(Long chatRoomId);
	Long countByChatRoomId(Long chatRoomId);
	ChatMessage findByMessageId(Long messageId);
};
