package org.example.expert.domain.chat.controller;

import org.example.expert.domain.chat.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller    // 이 클래스가 Spring MVC의 컨트롤러 역할을 함을 나타냅니다. (웹소켓 메시지 처리도 포함)
@RequiredArgsConstructor
@Slf4j
public class ChatController {

	// 서버가 클라이언트에게 특정 경로로 메시지를 보낼 때 사용하는 템플릿 클래스입니다.
	private final SimpMessagingTemplate messagingTemplate;

	/**
	 * 클라이언트가 메시지를 발행(발송)할 때 처리하는 메서드입니다.
	 * WebSocketConfig에서 발행(pub) 경로를 "/pub"으로 설정했으므로,
	 * 클라이언트는 실제로는 "/pub/chat.send" 경로로 메시지를 보내게 되고, 이 메서드가 실행됩니다.
	 */
	@MessageMapping("/chat.send")
	public void send(
		ChatMessage message,
		SimpMessageHeaderAccessor headerAccessor
	) {
		// 웹소켓에 연결된 현재 클라이언트의 고유한 세션 ID를 가져옵니다.
		String sessionId = headerAccessor.getSessionId();

		log.info("서버 수신 메시지, 세션 ID = {}, 전송자 = {}, 내용 = {}", sessionId, message.getSender(), message.getContent());

		// 수신한 메시지를 "/sub/chat" 경로를 구독하고 있는 모든 클라이언트에게 전송합니다.
		// "/sub/chat"이라는 채팅방에 들어와 있는 사람들의 화면에 메시지가 나타나게 됩니다.
		messagingTemplate.convertAndSend("/sub/chat", message);
	}
}
