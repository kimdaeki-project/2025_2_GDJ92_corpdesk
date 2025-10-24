package com.goodee.corpdesk.notification.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.goodee.corpdesk.notification.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findByNotificationTypeAndUsernameAndIsReadFalseOrderByNotificationIdDesc(String type, String username);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("""
		UPDATE Notification n SET isRead = true , n.updatedAt = CURRENT_TIMESTAMP 
		WHERE n.username = :username AND n.notificationType =:notificationType AND n.relatedId =:relatedId
		""")
	int updateReadTrue(@Param("relatedId")Long relatedId,@Param("notificationType")String notificationType,@Param("username") String username);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("""
			UPDATE Notification n SET isRead = true , n.updatedAt = CURRENT_TIMESTAMP 
			WHERE n.username = :username AND n.notificationType =:notificationType
			""")
	int updateAllReadTrue(@Param("notificationType")String notificationType,@Param("username")  String username);


	List<Notification> findByRelatedIdAndNotificationTypeAndUsername(Long relatedId, String notificationType, String username);

	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("""
	    UPDATE Notification n
	       SET n.isRead = true,
	           n.updatedAt = CURRENT_TIMESTAMP
	     WHERE n.username = :username
	       AND n.notificationType = 'message'
	       AND n.relatedId IN (
	           SELECT c.messageId FROM ChatMessage c WHERE c.chatRoomId = :chatRoomId
	       )
	""")
	int updateOneRoomAllReadTrue(@Param("chatRoomId")Long chatRoomId,@Param("username") String username);

	
}
