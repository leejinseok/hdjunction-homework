package com.hdjunction.homework.core.db.domain.patient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Long countByHospitalId(Long hospitalId);

    @Query("select p from Patient p join fetch p.visits where p.id = :patientId")
    Optional<Patient> findByIdFetchVisits(Long patientId);
}
