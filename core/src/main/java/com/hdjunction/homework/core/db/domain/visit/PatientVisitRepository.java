package com.hdjunction.homework.core.db.domain.visit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PatientVisitRepository extends JpaRepository<PatientVisit, Long> {

    @Query("select pv from PatientVisit pv where pv.patient.id = :patientId")
    List<PatientVisit> findAllByPatientId(Long patientId);
}
