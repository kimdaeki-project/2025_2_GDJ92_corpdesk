package com.goodee.corpdesk.websocket;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class AuthHandshakeInterceptor implements HandshakeInterceptor {
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		
		//ServletServerHttpRequest는 ServerHttpRequest의 구현체임으로 다운캐스팅해서 사용해야함
		if(request instanceof ServletServerHttpRequest servletRequest ) {
			
			//HttpServletRequest 객체를 얻음 그 객체에서 쿠키에 접근 가능
			HttpServletRequest httpRequest = servletRequest.getServletRequest();
			
			Cookie[] cookies = httpRequest.getCookies();
			if(cookies !=null) {
				for(Cookie cookie : cookies) {
					if("accessToken".equals(cookie.getName())) {
						String token = cookie.getValue();
						
						//stompHandler에서 accessor.getSessionAttributes().get("accessToken")으로 꺼낼수있음
						attributes.put("token", token);
						
						
						
					}
				}
			}
		}
		
		
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {

	}
}
