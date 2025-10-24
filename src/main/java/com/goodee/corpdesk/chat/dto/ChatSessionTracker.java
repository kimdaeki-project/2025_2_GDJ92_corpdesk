package com.goodee.corpdesk.chat.dto;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
@Component
public class ChatSessionTracker {
	//채팅은 여러 사용자가 동시에 입장/퇴장할 수 있음 → 동시성 문제가 생김
	//일반 HashMap은 멀티스레드 환경에서 데이터 깨짐 / ConcurrentModificationException 발생 가능 그래서 ConcuurentHashMap 사용
    private final Map<Long, Set<String>> activeUsers = new ConcurrentHashMap<>();
    
    private final Map<String,SessionInfo> sessionMap = new ConcurrentHashMap<>();
    
    public void addUser(Long roomId, String username ,String sessionId) {
    	//roomId가 있으면 해당 roomId의 set을반환하고 없으면 새로운 set을 생성해서 Map에 넣음
    	//computeIfAbsent 동작 원리
    	//V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction)
    	//key가 이미 존재하면: 해당 value(=Set)를 그대로 반환
    	//key가 없으면: mappingFunction.apply(key) 실행해서 새 value를 만들고 → Map에 put(key, value) 한 뒤 → 그 value를 반환
        activeUsers.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(username);
       //해당 websokect session 에 방번호랑 유저를 같이 map에 저장함
        sessionMap.put(sessionId, new SessionInfo(roomId, username ,true));
        
    }

    public void removeUserBySession(String sessionId) {
    	//세션이 있으면 그세션에 매핑되는 roomid랑 username을 가져와서 활동중인 목록에서 지움
    	SessionInfo info = sessionMap.remove(sessionId); 
    	if(info !=null) {
    		//roomId가 있으면 set을 반환하고 없으면 비어있는 set을 반환
    		activeUsers.getOrDefault(info.getChatRoomId(), Collections.emptySet()).remove(info.getUsername());
    	}
    	
        
    }

    public boolean isUserActive(Long roomId, String username) {
        return activeUsers.getOrDefault(roomId, Collections.emptySet()).contains(username);
    }
    public void setFocus(String sessionId, boolean focused) {
        SessionInfo info = sessionMap.get(sessionId);
        if (info != null) {
            info.setFocused(focused);
        }
    }

    public boolean isUserFocused(Long roomId, String username) {
        return activeUsers.getOrDefault(roomId, Collections.emptySet())
                          .contains(username) &&
               sessionMap.values().stream()
                   .anyMatch(i -> i.getChatRoomId().equals(roomId)
                               && i.getUsername().equals(username)
                               && i.isFocused());
    }
    
}
