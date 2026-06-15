package org.example.expert.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

// Querydsl은 기본적으로 DB와 직접적으로 통신하는 기능이 없다
// 이를 QueryDsl을 만든 곳에서 DB와 통신할 수 있게 하기 위해서 이 설정 코드의 EntityManager를 넘겨받아야만 한다.
@Configuration
public class QuerydslConfig {

	// JPA 환경에서 EntityManager를 주입받기 위한 어노테이션
	@PersistenceContext
	private EntityManager em;

	@Bean
	public JPAQueryFactory jpaQueryFactory() {
		return new JPAQueryFactory(em);
	}
}
