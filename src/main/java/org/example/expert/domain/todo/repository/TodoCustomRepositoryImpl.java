package org.example.expert.domain.todo.repository;

import static org.example.expert.domain.comment.entity.QComment.*;
import static org.example.expert.domain.manager.entity.QManager.*;
import static org.example.expert.domain.todo.entity.QTodo.*;
import static org.example.expert.domain.user.entity.QUser.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
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

	@Override
	public Page<TodoSearchResponse> searchByFilter(Pageable pageable, String title, LocalDateTime startAt,
		LocalDateTime endAt, String nickname) {
		BooleanBuilder booleanBuilder = new BooleanBuilder();

		if (title != null) {
			booleanBuilder.and(todo.title.contains(title));
		}

		if (startAt != null) {
			booleanBuilder.and(todo.modifiedAt.goe(startAt));
		}

		if (endAt != null) {
			booleanBuilder.and(todo.modifiedAt.loe(endAt));
		}

		if (nickname != null) {
			booleanBuilder.and(user.nickname.contains(nickname));
		}

		// - 일정에 대한 모든 정보가 아닌, 제목만 넣어주세요.
		// - 해당 일정의 담당자 수를 넣어주세요.
		// - 해당 일정의 총 댓글 개수를 넣어주세요.
		// DTO 프로젝션으로 필요한 데이터만 뽑아서 바로 객체로 만들어서 반환
		List<TodoSearchResponse> content = queryFactory
			.select(Projections.constructor(TodoSearchResponse.class,
				todo.title,
				manager.countDistinct(),
				comment.countDistinct()
			))
			.from(todo)
			.leftJoin(todo.user, user)
			.leftJoin(manager).on(manager.todo.eq(todo))
			.leftJoin(comment).on(comment.todo.eq(todo))
			.where(booleanBuilder)
			.groupBy(todo.id, todo.title)
			.orderBy(todo.modifiedAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory
			.select(todo.countDistinct())
			.from(todo)
			.leftJoin(todo.user, user)
			.where(booleanBuilder)
			.fetchOne();

		if (total == null)
			total = 0L;

		return new PageImpl<>(content, pageable, total);
	}
}
