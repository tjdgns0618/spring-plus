package org.example.expert.domain.todo.repository;

import static org.example.expert.domain.todo.entity.QTodo.*;
import static org.example.expert.domain.user.entity.QUser.*;

import java.util.Optional;

import org.example.expert.domain.todo.entity.Todo;

import com.querydsl.jpa.JPQLQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TodoCustomRepositoryImpl implements TodoCustomRepository {

	private final JPQLQueryFactory queryFactory;

	@Override
	public Optional<Todo> findByIdWithUser(Long todoId) {
		Todo result = queryFactory
			.selectFrom(todo)
			// 할일 엔티티에 관련된 user를 LeftJoin 하고 fetchJoin으로 모두 영속성 컨텍스트로 불러오기
			.leftJoin(todo.user, user).fetchJoin()
			.where(todo.id.eq(todoId))
			// 단건 조회 수행, 조건에 맞는 결과가 없을 경우 null
			.fetchOne();

		return Optional.ofNullable(result);
	}
}
