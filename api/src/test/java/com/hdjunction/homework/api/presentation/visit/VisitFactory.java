package com.hdjunction.homework.api.presentation.visit;

import com.hdjunction.homework.api.presentation.patient.PatientFactory;
import com.hdjunction.homework.core.db.domain.patient.Patient;
import com.hdjunction.homework.core.db.domain.visit.PatientVisit;

import java.time.LocalDateTime;
import java.util.List;

public class VisitFactory {

    public static List<PatientVisit> getTestPatientVisits() {
        return List.of(getTestPatientVisit());
    }

    public static PatientVisit getTestPatientVisit() {
        Patient testPatient = PatientFactory.getTestPatient();

        return PatientVisit.builder()
                .id(1L)
                .patient(testPatient)
                .hospital(testPatient.getHospital())
                .receptionDateTime(LocalDateTime.now())
                .build();
    }
}
