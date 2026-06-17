package org.example.expert.domain.log.service;

import org.example.expert.domain.log.entity.Log;
import org.example.expert.domain.log.repository.LogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LogService {

	private final LogRepository logRepository;

	@Transactional(propagation = Propagation.REQUIRED)
	public void saveLog(
		Long todoId,
		String todoTitle,
		Long todoOwnerId,
		String ownerName,
		Long managerUserId,
		String managerUserName
	) {
		logRepository.save(new Log(todoId, todoTitle, todoOwnerId, ownerName, managerUserId, managerUserName));
	}
}
