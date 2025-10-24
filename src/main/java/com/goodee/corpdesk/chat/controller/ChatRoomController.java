package com.goodee.corpdesk.chat.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodee.corpdesk.chat.dto.ChatContact;
import com.goodee.corpdesk.chat.dto.ChatMessageDto;
import com.goodee.corpdesk.chat.dto.RoomData;
import com.goodee.corpdesk.chat.entity.ChatRoom;
import com.goodee.corpdesk.chat.service.ChatRoomService;
import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeService;
import com.goodee.corpdesk.position.entity.Position;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Chat", description = "채팅 관련 API")
@Controller
@RequestMapping("/chat/room/**")
public class ChatRoomController {

	@Autowired
	ChatRoomService chatRoomService;
	@Autowired
	EmployeeService employeeService;
	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;

    @Value("${cat.chat}")
    private String cat;

    @ModelAttribute("cat")
    public String getCat() {
        return cat;
    }

	@GetMapping("list")
	public String chatRoomList(Principal principal ,Model model) {
			String username= principal.getName();
			List<RoomData> roomList= chatRoomService.getChatRoomList(username);
			List<ChatContact> contactList = chatRoomService.getContactList();
			contactList.sort((o1, o2) ->{
				return o1.getName().compareTo(o2.getName());
			});
			model.addAttribute("roomList", roomList);
			model.addAttribute("contactList",contactList);
		return "Chat/chat_list";
	}
	
	@Operation(summary = "채팅방 내부", description = "그룹,개인 채팅방을 엽니다")
	@GetMapping("detail/{roomId}")
	public String chatRoomDetail(@PathVariable(value = "roomId") Long roomId , Model model , Principal principal) throws JsonProcessingException {
		RoomData roomData = new RoomData();
		roomData = chatRoomService.chatRoomDetail(roomId,principal);
		
		//없는 방 조회시
		if(roomData==null) {
			//TODO
			//에러 창으로 이동
			return "Chat/chat_error";
		}
		
		//json으로 변환
		ObjectMapper mapper = new ObjectMapper();
		String JsonRoomData = mapper.writeValueAsString(roomData);
		List<ChatContact> contactList = chatRoomService.getContactList();
		contactList.sort((o1, o2) ->{
			return o1.getName().compareTo(o2.getName());
		});
		model.addAttribute("roomData",JsonRoomData);
		model.addAttribute("employeeList",contactList);
		model.addAttribute("roomDataEl",roomData);
		return "Chat/chat_page";
	}
	@Operation(summary = "방 생성", description = "그룹,개인 채팅방을 생성 합니다")
	@ResponseBody
	@PostMapping("createRoom")
	public RoomData createRoom(@RequestBody RoomData roomdata , Principal principal) {
		//채팅방 생성 
		Long roomId =chatRoomService.createRoom(roomdata, principal);
		RoomData chatRoom = new RoomData();
		chatRoom.setChatRoomId(roomId);

		return chatRoom;
	}
	@Operation(summary = "방 삭제", description = "그룹,개인 채팅방을 삭제 합니다")
	@GetMapping("out/{roomId}")
	@ResponseBody
	public boolean chatRoomOut(@Parameter(description = "나갈 방 ID",example = "1",required = true) @PathVariable(value = "roomId") Long roomId,Principal principal) {
		boolean result = chatRoomService.outRoom(roomId,principal);
		return result;
	}
	
	@Operation(summary = "채팅 인원 초대", description = "개인 , 그룹 채팅방에 인원을 초대 합니다")
	@PostMapping("inviteRoom")
	@ResponseBody
	public boolean inviteRoom(@RequestBody RoomData roomData , Principal principal) {
		boolean result = chatRoomService.inviteRoom(roomData , principal);
		return result;
	}
}
