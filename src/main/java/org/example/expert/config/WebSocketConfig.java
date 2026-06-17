package org.example.expert.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker	// 웹소켓 서버를 활성화하고, STOMP 메시지 브로커를 사용할 수 있게 해줍니다.
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	/**
	 * 메시지 브로커에 관련된 설정을 하는 메서드입니다.
	 * 클라이언트에서 다른 클라이언트로 메시지를 라우팅 할 때 사용하는 브로커를 구성합니다.
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// 메시지 구독 요청의 경로를 "/sub"로 설정합니다.
		registry.enableSimpleBroker("/sub");
		// 메시지 발행 요청의 경로 "/pub"로 설정합니다.
		registry.setApplicationDestinationPrefixes("/pub");
	}

	/**
	 * 클라이언트가 웹소켓 서버에 연결(Connect)할 수 있는 엔드포인트를 설정하는 메서드입니다.
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// 최초 웹소켓 연결을 위한 엔드포인트를 "/ws"로 설정합니다.
		registry.addEndpoint("/ws")
			.setAllowedOriginPatterns("*")
		;
	}
}
