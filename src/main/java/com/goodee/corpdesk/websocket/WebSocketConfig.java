package com.goodee.corpdesk.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Autowired
	private StompHandler stompHandler;
@Override
public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/sub","/queue"); //구독 및 알림 기능 prefix
		registry.setApplicationDestinationPrefixes("/pub"); //발행 prefix
		registry.setUserDestinationPrefix("/user");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry
				.addEndpoint("/ws")
				.addInterceptors(new AuthHandshakeInterceptor()) // 쿠키를 읽어오기 위함
				.setAllowedOriginPatterns("*")
				.withSockJS();
	}
	@Override
		public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(stompHandler);
	}
}
