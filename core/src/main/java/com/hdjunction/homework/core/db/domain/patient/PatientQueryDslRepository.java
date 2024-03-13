package com.hdjunction.homework.core.db.domain.patient;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.hdjunction.homework.core.db.domain.patient.QPatient.patient;

@Repository
@RequiredArgsConstructor
public class PatientQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<Patient> findAll(final PatientSearchType searchType, final String query, final Pageable pageable) {
        JPAQuery<Patient> from = jpaQueryFactory
                .select(patient)
                .from(patient)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        if (searchType != null && query != null) {
            if (searchType == PatientSearchType.NAME) {
                from.where(patient.name.contains(query));
            } else if (searchType == PatientSearchType.BIRTH) {
                from.where(patient.birth.eq(query));
            } else if (searchType == PatientSearchType.REGISTRATION_NUMBER) {
                from.where(patient.registrationNumber.eq(query));
            }
        }

        List<Patient> content = from.fetch();
        long count = jpaQueryFactory.from(patient).select(patient.count()).fetchOne();
        return new PageImpl<>(content, pageable, count);
    }

}
