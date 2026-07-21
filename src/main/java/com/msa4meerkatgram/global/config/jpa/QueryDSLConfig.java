package com.msa4meerkatgram.global.config.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Configuration 클래스 정의, Spring이 애플리케이션 컨텍스트를 초기화할 때, 이 클래스에 정의된 빈(Bean)들을 등록
@Configuration
public class QueryDSLConfig {
    // JPA에서 DB와 상호작용을 하기 위한 객체 EntityManager를 Spring 컨텍스트에 자동으로 주입
    // (JPA와 관련된 컨텍스트를 연결해 줌)
    // @Autowired와 비슷한 역할이지만, @Autowired는 빈을 주입하는 반면, @PersistenceContext는 JPA 컨텍스트와 관련된 객체를 주입)
    @PersistenceContext
    private EntityManager entityManager;
    // Entity의 영속성 관리를 담당하는 JPA의 핵심 인터페이스
    // CRUD 작업, 쿼리 실험 등 DB와의 상호작용을 담당

    // Spring의 빈으로 등록
    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        // JPAQueryFactory : QueryDSL을 사용하기 위해 필요한 객체
        return new JPAQueryFactory(entityManager);
    }
}

