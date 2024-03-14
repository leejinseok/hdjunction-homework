package com.hdjunction.homework.core.db.domain.visit;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.hdjunction.homework.core.db.domain.patient.QPatient.patient;
import static com.hdjunction.homework.core.db.domain.visit.QPatientVisit.patientVisit;

@Repository
@RequiredArgsConstructor
public class PatientVisitQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<PatientVisit> findAll(final PatientVisitSearchType searchType, final String query, final Pageable pageable) {
        JPAQuery<PatientVisit> from = jpaQueryFactory
                .select(patientVisit)
                .from(patientVisit)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        if (searchType != null && query != null) {
            if (searchType == PatientVisitSearchType.PATIENT_ID) {
                from.where(patientVisit.patient.id.eq(Long.parseLong(query)));
            } else if (searchType == PatientVisitSearchType.HOSPITAL_ID) {
                from.where(patientVisit.hospital.id.eq(Long.parseLong(query)));
            }
        }

        List<PatientVisit> content = from.fetch();
        long count = jpaQueryFactory.from(patient).select(patient.count()).fetchOne();
        return new PageImpl<>(content, pageable, count);
    }

}
