package org.example.expert.domain.chat.controller;

import org.example.expert.domain.chat.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

	private final SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/chat.send")
	public void send(
		ChatMessage message,
		SimpMessageHeaderAccessor headerAccessor
	) {
		String sessionId = headerAccessor.getSessionId();

		log.info("서버 수신 메시지, 세션 ID = {}, 전송자 = {}, 내용 = {}", sessionId, message.getSender(), message.getContent());

		messagingTemplate.convertAndSend("/sub/chat", message);
	}
}
