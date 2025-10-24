package com.goodee.corpdesk.chat.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.goodee.corpdesk.employee.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.goodee.corpdesk.chat.dto.ChatSessionTracker;
import com.goodee.corpdesk.chat.dto.FocusMessage;
import com.goodee.corpdesk.chat.entity.ChatMessage;
import com.goodee.corpdesk.chat.service.ChatMessageService;

@Controller
@RequestMapping("/chat/message/**")
public class ChatMessageController {



	@Autowired
	// Spring에서 서버가 클라이언트(STOMP 구독자)에게 메시지를 푸시하기 위해 제공하는 템플릿 객체
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private ChatMessageService chatMessageService;
	@Autowired
	private ChatSessionTracker chatSessionTracker;

    @Value("${cat.chat}")
    private String cat;

    @ModelAttribute("cat")
    public String getCat() {
        return cat;
    }

  
  
	// websocket 요청에 대한 매핑 위의 requestMapping과 관련없고 websocket config에서 지정해준 prefix 사용
	@MessageMapping("/chat/message")
	public void chatsendMessage(ChatMessage msg , Principal principal) {
		chatMessageService.processMessage(msg,principal);
	    
	}

	
	@MessageMapping("/chat/focus")
	 public void updateFocus(FocusMessage message, Principal principal, StompHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();
        chatSessionTracker.setFocus(sessionId, message.isFocused());
        
        //포커스되면 읽음을 목록 jsp에도 보내줘야함
       if(message.isFocused()) {
    	  message.setNotificationType("read");
       	messagingTemplate.convertAndSendToUser(principal.getName(),"/queue/notifications",message);
       }
	}
	
	@GetMapping("list/{chatRoomId}/{messageNo}/{size}")
	@ResponseBody
	public Map<String , Object> chatMessageList(@PathVariable(value = "chatRoomId")Long chatRoomId , 
			@PathVariable(value = "messageNo")Long lastMessageNo, @PathVariable(value = "size")int size , Principal principal){
		
		List<ChatMessage> list = chatMessageService.chatMessageList(chatRoomId,lastMessageNo,size,principal);
		Map<String, Object> map = new HashMap<>();
		map.put("size", list.size());
		map.put("messages",list);
		
		return map;
		
	}
	
	
	
	
}
