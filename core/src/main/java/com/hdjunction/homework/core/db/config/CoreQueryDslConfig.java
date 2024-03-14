package com.hdjunction.homework.core.db.config;


import com.hdjunction.homework.core.db.domain.patient.PatientQueryDslRepository;
import com.hdjunction.homework.core.db.domain.visit.PatientVisitQueryDslRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        PatientQueryDslRepository.class,
        PatientVisitQueryDslRepository.class
})
public class CoreQueryDslConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

}
