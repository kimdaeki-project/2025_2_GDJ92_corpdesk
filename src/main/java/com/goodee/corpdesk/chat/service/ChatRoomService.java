package com.goodee.corpdesk.chat.service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.goodee.corpdesk.employee.dto.EmployeeListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.goodee.corpdesk.chat.controller.ChatParticipantController;
import com.goodee.corpdesk.chat.dto.ChatContact;
import com.goodee.corpdesk.chat.dto.ChatMessageDto;
import com.goodee.corpdesk.chat.dto.ChatSessionTracker;
import com.goodee.corpdesk.chat.dto.RoomData;
import com.goodee.corpdesk.chat.entity.ChatMessage;
import com.goodee.corpdesk.chat.entity.ChatParticipant;
import com.goodee.corpdesk.chat.entity.ChatRoom;
import com.goodee.corpdesk.chat.repository.ChatMessageRepository;
import com.goodee.corpdesk.chat.repository.ChatParticipantRepository;
import com.goodee.corpdesk.chat.repository.ChatRoomRepository;
import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeRepository;
import com.goodee.corpdesk.employee.EmployeeService;
import com.goodee.corpdesk.file.entity.EmployeeFile;
import com.goodee.corpdesk.notification.dto.NotificationDto;
import com.goodee.corpdesk.notification.service.NotificationService;
import com.goodee.corpdesk.position.entity.Position;

@Service
public class ChatRoomService {

	@Autowired
    private ChatParticipantService chatParticipantService;
	@Autowired
	EmployeeService employeeService;
	@Autowired
	private ChatRoomRepository chatRoomRepository;
	@Autowired
	private ChatParticipantRepository chatParticipantRepository;
	@Autowired
	ChatMessageRepository chatMessageRepository;
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired
	private ChatSessionTracker chatSessionTracker;
	@Autowired
	private NotificationService notificationService;
  
	
	

	//유저이름으로 유저 이름 부서 직위 가져옴
		public String getUserNameDepPos(String username) {
			Map<String, Object> map = employeeService.detail(username);
	        Employee emp = (Employee) map.get("employee");
	        Department department = (Department) map.get("department");
	        Position position = (Position) map.get("position");
	        String userNameDepPos=null;
	        if(department==null) {
	        	userNameDepPos = "-";
	        }else {
	        	userNameDepPos =department.getDepartmentName();
	        }
	        if(position==null) {
	        	userNameDepPos=userNameDepPos+" - ";
	        }else {
	        	userNameDepPos=userNameDepPos+" "+position.getPositionName();
	        }
	        
	         userNameDepPos = userNameDepPos+ " " + emp.getName();
	        return userNameDepPos;
			
		}
	//개인 이미지
		public String getUserImgPath(String username) {
			Optional<EmployeeFile> empFileOp = employeeService.getEmployeeFileByUsername(username);
			if (empFileOp.isPresent()) {
				EmployeeFile empFile = empFileOp.get();
				if (empFile != null && empFile.getUseYn()) {
					return "/files/profile/" + empFile.getSaveName() + "." + empFile.getExtension();
				} else {
					return "/images/default_profile.jpg";
				}
			} else {
				return "/images/default_profile.jpg";
			}
		}
	
	//해당 유저의 모든 채팅방 목록을 불러옴
	public List<RoomData> getChatRoomList(String username) {
		
		//해당 유저의 모든 채팅방 목록을 가져옴
		List<ChatRoom> listE= chatRoomRepository.findAllByUsername(username);
		List<RoomData> list = new ArrayList<>();
		listE.forEach(le->{
			RoomData data = new RoomData();
			data.setChatRoomId(le.getChatRoomId());
			
			data.setChatRoomType(le.getChatRoomType());
			data.setChatRoomTitle(le.getChatRoomTitle());
			data.setUnreadCount(le.getUnreadCount());		
			
			
			// 해당 방의 마지막 메세지 내용, 시간을 가져옴
			
			ChatMessage chatMessage = chatMessageRepository.findTopByChatRoomIdOrderByMessageIdDesc(data.getChatRoomId());
			if(chatMessage==null) {
				data.setLastMessageTime(null);
				data.setChatRoomLastMessage(" ");			
				
			}else {
				data.setLastMessageTime(chatMessage.getSentAt());
				data.setChatRoomLastMessage(chatMessage.getMessageContent());			
				
			}
			
			list.add(data);
		});
		
		//각 채팅방 별로 어떤 메세지 까지 읽었는 지를 확인 후 계산해서 list에 다시 넣고 반환
		//이 때 처음 생성되서 lastCheck가 null인경우 해당 방의 모든 메세지의 개수를 가져옴
		list.forEach(l->{
			Long roomId = l.getChatRoomId();
			ChatParticipant cp= chatParticipantRepository.findByChatRoomIdAndEmployeeUsername(roomId, username);
			
			//1대1채팅일경우 채팅방 제목 및 프로필 이미지를 상대방으로 설정 
			if(l.getChatRoomType().equals("direct")) {
				List<ChatParticipant> cps = chatParticipantRepository.findAllByChatRoomId(roomId);
				if(cps.size()==1) {
					//채팅방 이름 설정
					
					String roomtitle = getUserNameDepPos(username);
					//프로필 이미지 설정
					Optional <EmployeeFile> empFileOp= employeeService.getEmployeeFileByUsername(username);
				        if(empFileOp.isPresent()) {
				        	EmployeeFile empFile = empFileOp.get();
				        	 if(empFile!=null && empFile.getUseYn()) {
				 	        	l.setImgPath("/files/profile/"+empFile.getSaveName()+"."+empFile.getExtension());
				 	        }else {
				 	        	l.setImgPath("/images/default_profile.jpg");
				 	        }
				        }else {
				        	l.setImgPath("/images/default_profile.jpg");
				        }
				       
					l.setChatRoomTitle(roomtitle);
				}else {
					for(ChatParticipant c : cps) {
						
						if(!c.getEmployeeUsername().equals(username)) {
							//채팅방이름을 설정
							String roomtitle = getUserNameDepPos(c.getEmployeeUsername());
							//프로필 이미지 설정
							Optional <EmployeeFile> empFileOp= employeeService.getEmployeeFileByUsername(c.getEmployeeUsername());
					        if(empFileOp.isPresent()) {
					        	EmployeeFile empFile = empFileOp.get();
					        	 if(empFile!=null && empFile.getUseYn()) {
					 	        	l.setImgPath("/files/profile/"+empFile.getSaveName()+"."+empFile.getExtension());
					 	        }else {
					 	        	l.setImgPath("/images/default_profile.jpg");
					 	        }
					        }else {
					        	l.setImgPath("/images/default_profile.jpg");
					        }
							l.setChatRoomTitle(roomtitle);
							break;
						}
					}
				}
			}else {
				//그룹채팅일 경우 이미지를 동일하게 맞춤
				l.setImgPath("/images/group_profile.png");
			}
			
			if(cp.getLastCheckMessageId()==null) {
				l.setUnreadCount((chatParticipantRepository.countAll(roomId)));
			}
			else {
				l.setUnreadCount(chatParticipantRepository.count(cp.getLastCheckMessageId(),roomId));
			}
		});
		
		
		return list;
	}

	
	
	//채팅방 생성
	public Long createRoom(RoomData roomdata , Principal principal ) {
		List<String> usernames = roomdata.getUsernames();
		ChatRoom chatroom = new ChatRoom();
		chatroom.setUseYn(true);
		ChatParticipant chatParticipant = new ChatParticipant();
		
		//1대1 채팅방 생성
		if(usernames.size()==1) {
			//중복먼저 확인
			//채팅방 상대랑 , 사용자를 넣고 확인해옴
			Optional<ChatRoom> directRoom;
			//대상이 자기 자신인 경우
			if(usernames.getFirst().equals(principal.getName())) {
				directRoom = chatRoomRepository.findDuplicatedRoomOwn(principal.getName());
				
				if(!directRoom.isEmpty()) {
					return directRoom.get().getChatRoomId();
				}
				else {
					//채팅방 타입 설정
					chatroom.setChatRoomType(roomdata.getChatRoomType());
					chatroom = chatRoomRepository.save(chatroom);
					
					//사용자 저장
					chatParticipant = new ChatParticipant();
					chatParticipant.setChatRoomId(chatroom.getChatRoomId());
					chatParticipant.setEmployeeUsername(principal.getName());
					chatParticipant.setUseYn(false);
					chatParticipantRepository.save(chatParticipant);
				}
				
			// 상대방이 있는 경우
			}else {
				directRoom =chatRoomRepository.findDuplicatedRoom(principal.getName(),usernames.getFirst());
				if(!directRoom.isEmpty()) {
					
					return directRoom.get().getChatRoomId();
				}
				else {
					//채팅방 타입 설정
					chatroom.setChatRoomType(roomdata.getChatRoomType());
					chatroom = chatRoomRepository.save(chatroom);
					
					//상대방 저장
					chatParticipant.setEmployeeUsername(usernames.getFirst());
					chatParticipant.setChatRoomId(chatroom.getChatRoomId());
					chatParticipant.setUseYn(false);
					chatParticipantRepository.save(chatParticipant);
					
					//사용자 저장
					chatParticipant = new ChatParticipant();
					chatParticipant.setChatRoomId(chatroom.getChatRoomId());
					chatParticipant.setEmployeeUsername(principal.getName());
					chatParticipant.setUseYn(false);
					chatParticipantRepository.save(chatParticipant);
				}
				
			}
			
			
		}
		//그룹채팅 생성
		else {
			chatroom.setChatRoomTitle(roomdata.getChatRoomTitle());
			chatroom.setChatRoomType(roomdata.getChatRoomType());
			chatroom = chatRoomRepository.save(chatroom);
			
			for(String user : roomdata.getUsernames()) {
				chatParticipant = new ChatParticipant();
				chatParticipant.setChatRoomId(chatroom.getChatRoomId());
				chatParticipant.setEmployeeUsername(user);
				chatParticipant.setUseYn(true);
				chatParticipantRepository.save(chatParticipant);
			}
			//사용자 저장
			chatParticipant = new ChatParticipant();
			chatParticipant.setChatRoomId(chatroom.getChatRoomId());
			chatParticipant.setEmployeeUsername(principal.getName());
			chatParticipant.setUseYn(true);
			chatParticipantRepository.save(chatParticipant);
			chatParticipantRepository.flush();
			
			
			Long roomId = chatroom.getChatRoomId();
			ChatMessage msg = new ChatMessage();
		    msg.setChatRoomId(roomId);
		    msg.setMessageType("create");
		    msg.setMessageContent("새로운 채팅방이 생성되었습니다.");
		    msg.setEmployeeUsername(principal.getName());
		    msg.setUseYn(true);
		    ChatMessageDto saveMsg = chatMessageRepository.save(msg).toChatMessageDto();
		    saveMsg.setViewName(chatroom.getChatRoomTitle());
		    saveMsg.setNotificationType("create");
		    saveMsg.setImgPath("/images/group_profile.png");
		    
		    
		    
		    
		    chatParticipantService.updateLastMessage(principal.getName(), roomId);
		    messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/notifications", saveMsg);
		    for(String user : roomdata.getUsernames()) {
		    	notificationService.saveNotification(saveMsg.getMessageId(),"message",user);
				  messagingTemplate.convertAndSendToUser(user, "/queue/notifications", saveMsg);
		    }
			
		}
		return chatroom.getChatRoomId();
	}


@Transactional
	public boolean outRoom(Long roomId, Principal principal) {
		boolean result =false;
		if(chatParticipantRepository.existsByChatRoomIdAndEmployeeUsernameAndUseYnTrue(roomId, principal.getName())) {
			chatParticipantRepository.updateRoomUseYnFalse(principal.getName(), roomId);
			chatParticipantService.updateLastMessage(principal.getName(), roomId);
			
			chatParticipantRepository.flush();
			//알림 목록에서 제거 
			notificationService.setNotificationOneRoomReadAll(roomId, principal.getName());
			//그룹 채팅방 일때만 퇴장 메세지를 보내줌
			if(chatRoomRepository.findByChatRoomId(roomId).get().getChatRoomType().equals("room")) {
				//퇴장 메세지 저장
		    	ChatMessage msg = new ChatMessage();
			    msg.setChatRoomId(roomId);
			    msg.setMessageType("out");
			    msg.setMessageContent(getUserNameDepPos(principal.getName())+"님이 퇴장하였습니다.");
			    msg.setUseYn(true);
			    ChatMessageDto saveMsg = chatMessageRepository.save(msg).toChatMessageDto();
			    saveMsg.setEmployeeUsername(principal.getName());
			    saveMsg.setViewName(getUserNameDepPos(principal.getName()));
			    saveMsg.setNotificationType("out");
			    messagingTemplate.convertAndSend("/sub/chat/room/"+roomId,saveMsg);
			    
			    List<ChatParticipant> participant =chatParticipantRepository.findAllByChatRoomIdAndUseYnTrue(roomId);
				 //개인 채팅목록에 알림을 보내줌
			    participant.forEach(p->{
			    	  if (!chatSessionTracker.isUserFocused(roomId, p.getEmployeeUsername())) {
			    		  saveMsg.setFocused(false);
			    		  notificationService.saveNotification(saveMsg.getMessageId(),"message",p.getEmployeeUsername());
			          } else {
			        	  saveMsg.setFocused(true);
			          }
			  	    
			          messagingTemplate.convertAndSendToUser(p.getEmployeeUsername(), "/queue/notifications", saveMsg);
			    });
			}
			result =true;
			
		}
		return result;
	}



	public String getChatRoomType(Long chatRoomId) {
		ChatRoom chatRoom =chatRoomRepository.findByChatRoomId(chatRoomId).get();
		return chatRoom.getChatRoomType();
	}


	//해당방의 방제목 있던 없던 가져옴
	public String getRoomTitle(Long chatRoomId) {
		return chatRoomRepository.findByChatRoomId(chatRoomId).get().getChatRoomTitle();
		
	}
	
	
	public Long getRoomUnreadCount(Long chatRoomId) {
		return chatRoomRepository.findByChatRoomId(chatRoomId).get().getUnreadCount();
		
	}

	//해당방이 그룹인지 1대1인지 확인하고 제목을 가져와서 보여줌
	public String getRoomPageTitle(Long roomId , Principal principal) {
		String roomType = getChatRoomType(roomId);
		String roomTitle = null;
		if(roomType.equals("direct")) {
		}else{
			roomTitle=getRoomTitle(roomId);
		}
		
		
		return roomTitle;
	}


	public RoomData chatRoomDetail(Long roomId, Principal principal) {
		if(chatRoomRepository.findByChatRoomId(roomId).isEmpty()||!chatParticipantRepository.existsByChatRoomIdAndEmployeeUsername(roomId,principal.getName())) {
			return null;
		}
		if(getChatRoomType(roomId)==null) {
			return null;
		}
		RoomData roomData = new RoomData();
		String roomType = getChatRoomType(roomId);
		String username = principal.getName();
		String roomTitle = null;
		List<String> viewNameList = new ArrayList<>(); 
		List<String> usernames = new ArrayList<>();
		
		//1대1 채팅일경우 채팅방 제목 설정
		if(roomType.equals("direct")) {
			List<ChatParticipant> cps = chatParticipantRepository.findAllByChatRoomId(roomId);
			
			if(cps.size()==1) {
				roomTitle = getUserNameDepPos(username);
				viewNameList.add(roomTitle);
				usernames.add(username);
			}else {
				for(ChatParticipant c : cps) {
					//유저 이름을 바꿔서 화면에 목록을 보여주기 위함
					viewNameList.add(getUserNameDepPos(c.getEmployeeUsername()));
					usernames.add(c.getEmployeeUsername());
					//채팅방 제목은 상대편 이름
					if(!c.getEmployeeUsername().equals(username)) {
						roomTitle = getUserNameDepPos(c.getEmployeeUsername());
							
					}
				}
			}
			
		}else{
			//그룹 채팅은 활성화된 사람만 가져옴
			List<ChatParticipant> cps = chatParticipantRepository.findAllByChatRoomIdAndUseYnTrue(roomId);
			for(ChatParticipant c : cps) {
				//유저 이름을 바꿔서 화면에 목록을 보여주기 위함
				viewNameList.add(getUserNameDepPos(c.getEmployeeUsername()));
				usernames.add(c.getEmployeeUsername());
			}
			roomTitle=getRoomTitle(roomId);
		}
		roomData.setChatRoomType(roomType);
		roomData.setChatRoomId(roomId);
		roomData.setChatRoomTitle(roomTitle);
		roomData.setViewNameList(viewNameList);
		roomData.setUsernames(usernames);
		
		return roomData;
	}

	@Transactional
	public boolean inviteRoom(RoomData roomData , Principal principal) {
	    Optional<ChatRoom> optionalRoom = chatRoomRepository.findByChatRoomId(roomData.getChatRoomId());
	    
	    if (optionalRoom.isEmpty()) {
	    	return false;
	    }
	    //해당 방 처리
	    ChatRoom chatRoom = optionalRoom.get();

	    // 1대1 → room 전환
	    if (chatRoom.getChatRoomTitle() == null) {
	        chatRoom.setChatRoomTitle(roomData.getChatRoomTitle());
	        chatParticipantRepository.updateAllRoomUseYnTrue(roomData.getChatRoomId());
	    }
	    chatRoom.setChatRoomType("room");
	    
	    chatRoomRepository.flush();
	    
	    //해당방의 모든 사람 불러옴 전에 초대된적이있으면 useYn만 바꿔주면 되기때문에
	   
	    roomData.getUsernames().forEach(u->{
	    	ChatMessage lastMsg = chatMessageRepository.findTopByChatRoomIdOrderByMessageIdDesc(roomData.getChatRoomId());
	    	Long beforeInviteMessageId= null;
	    	//1대1 개설후 아무 메세지 안보내고 바로 초대할경
	    	if(lastMsg !=null) {
	    		beforeInviteMessageId=lastMsg.getMessageId();
	    	}
	    	// 해당 유저 정보에 방정보 저장
		    	ChatParticipant cp = chatParticipantRepository.findByChatRoomIdAndEmployeeUsername(roomData.getChatRoomId(), u);
		    	if(cp!=null) {
		    		cp.setChatRoomId(roomData.getChatRoomId());
			    	cp.setEmployeeUsername(u);
			    	cp.setLastCheckMessageId(beforeInviteMessageId);
			    	cp.setUseYn(true);
		    	}else {
		    		cp = new ChatParticipant();
		    		cp.setChatRoomId(roomData.getChatRoomId());
			    	cp.setEmployeeUsername(u);
			    	cp.setLastCheckMessageId(beforeInviteMessageId);
			    	cp.setUseYn(true);
		    	}
		    	chatParticipantRepository.save(cp);
		    	chatParticipantRepository.flush();
	    	 //초대 메세지 저장
	    	ChatMessage msg = new ChatMessage();
		    msg.setChatRoomId(roomData.getChatRoomId());
		    msg.setMessageType("enter");
		    msg.setMessageContent(getUserNameDepPos(u)+"님이 참가하였습니다.");
		    msg.setUseYn(true);
		    ChatMessageDto saveMsg = chatMessageRepository.save(msg).toChatMessageDto();
		    saveMsg.setEmployeeUsername(u);
		    saveMsg.setViewName(chatRoom.getChatRoomTitle());
		    saveMsg.setNotificationType("invite");
		    saveMsg.setImgPath("/images/group_profile.png");
		    saveMsg.setRoomName(getUserNameDepPos(u));
		    messagingTemplate.convertAndSend("/sub/chat/room/"+roomData.getChatRoomId(),saveMsg);
		    
		    
		    List<ChatParticipant> participant =chatParticipantRepository.findAllByChatRoomIdAndUseYnTrue(roomData.getChatRoomId());
			  //개인 채팅목록에 알림을 보내줌
			    participant.forEach(p->{
			    	  if (!chatSessionTracker.isUserFocused(roomData.getChatRoomId(), p.getEmployeeUsername())) {
			    		  notificationService.saveNotification(saveMsg.getMessageId(),"message",p.getEmployeeUsername());
			    		  saveMsg.setFocused(false);
			          } else {
			        	  saveMsg.setFocused(true);
			          }
			  	    
			          messagingTemplate.convertAndSendToUser(p.getEmployeeUsername(), "/queue/notifications", saveMsg);
			    });
	    			    
	    });
	    
	   
	  
	    
	  
	    
	    return true; 
	}


	public List<ChatContact> getContactList() {
		
		List<ChatContact> contactList= new ArrayList<>();
		List<EmployeeListDTO> emp = employeeService.getActiveEmployeesForList();
		emp.forEach(e->{
			if(!e.getUsername().equals("admin")) {
				ChatContact contact = new ChatContact();
				contact.setName(e.getName());
				contact.setDepartmentName(e.getDepartmentName());
				contact.setPositionName(e.getPositionName());
				contact.setUsername(e.getUsername());
				Optional<EmployeeFile> empF = employeeService.getEmployeeFileByUsername(e.getUsername());
				if(empF.isPresent()) {
					contact.setImgPath("/files/profile/"+empF.get().getSaveName()+"."+empF.get().getExtension());				
				}else {
					contact.setImgPath("/images/default_profile.jpg");
				}
				contactList.add(contact);
			}
			
			
			
		});
		
		return contactList;
	}

	//읽지 않은 메세지의 세부 정보들 가져옴 
	public List<ChatMessageDto> getChatNotificationList(String username) {
		List<ChatMessageDto> list =new ArrayList<>();
		List<NotificationDto> notificationList=notificationService.getMessageNotification(username);
		
		notificationList.forEach(nl->{
		ChatMessageDto msg =chatMessageRepository.findByMessageId(nl.getRelatedId()).toChatMessageDto();
		
		String roomTitle=null;
		if(getChatRoomType(msg.getChatRoomId()).equals("room")) {
			roomTitle = getRoomTitle(msg.getChatRoomId());
			msg.setImgPath("/images/group_profile.png");
		}else {
			roomTitle = getUserNameDepPos(msg.getEmployeeUsername());
			msg.setImgPath(getUserImgPath(msg.getEmployeeUsername()));
			
		}
		msg.setChatRoomId(msg.getChatRoomId());
		msg.setViewName(roomTitle);
		list.add(msg);
		});
		return list;
	}

	

}
