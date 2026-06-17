package org.example.expert.domain.log.entity;

import org.example.expert.domain.common.entity.Timestamped;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Log extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long todoId;
	private String todoTitle;

	private Long todoOwnerId;
	private String ownerName;

	private Long managerUserId;
	private String managerUserName;

	public Log(Long todoId, String todoTitle, Long todoOwnerId, String ownerName, Long managerUserId, String managerUserName) {
		this.todoId = todoId;
		this.todoTitle = todoTitle;
		this.todoOwnerId = todoOwnerId;
		this.ownerName = ownerName;
		this.managerUserId = managerUserId;
		this.managerUserName = managerUserName;
	}
}
