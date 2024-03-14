package com.hdjunction.homework.api.application.visit;

import com.hdjunction.homework.api.presentation.visit.dto.VisitRequest;
import com.hdjunction.homework.core.db.domain.hospital.Hospital;
import com.hdjunction.homework.core.db.domain.hospital.HospitalRepository;
import com.hdjunction.homework.core.db.domain.patient.Patient;
import com.hdjunction.homework.core.db.domain.patient.PatientRepository;
import com.hdjunction.homework.core.db.domain.visit.PatientVisit;
import com.hdjunction.homework.core.db.domain.visit.PatientVisitQueryDslRepository;
import com.hdjunction.homework.core.db.domain.visit.PatientVisitRepository;
import com.hdjunction.homework.core.db.domain.visit.PatientVisitSearchType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.hdjunction.homework.api.exception.ExceptionConstants.*;
import static com.hdjunction.homework.api.exception.NotFoundException.throwNotFoundException;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final PatientVisitRepository patientVisitRepository;
    private final PatientVisitQueryDslRepository patientVisitQueryDslRepository;
    private final PatientRepository patientRepository;
    private final HospitalRepository hospitalRepository;

    public Page<PatientVisit> findPage(final PatientVisitSearchType searchType, final String query, final Pageable pageable) {
        return patientVisitQueryDslRepository.findAll(searchType, query, pageable);
    }

    public PatientVisit findById(final Long id) {
        return patientVisitRepository.findById(id)
                .orElseThrow(
                        throwNotFoundException(NO_EXIST_PATIENT_VISIT.formatted(id))
                );
    }

    @Transactional
    public PatientVisit save(final VisitRequest request) {
        long patientId = request.getPatientId();
        Patient patient = patientRepository.findById(patientId).orElseThrow(
                throwNotFoundException(NO_EXIST_PATIENT.formatted(patientId))
        );

        long hospitalId = request.getHospitalId();
        Hospital hospital = hospitalRepository.findById(hospitalId).orElseThrow(
                throwNotFoundException(NO_EXIST_HOSPITAL.formatted(hospitalId))
        );

        PatientVisit visit = PatientVisit.builder()
                .patient(patient)
                .hospital(hospital)
                .receptionDateTime(request.getReceptionDateTime())
                .build();
        patient.updateRecentlyVisitDateTime(request.getReceptionDateTime());

        return patientVisitRepository.save(visit);
    }

    @Transactional
    public void deleteById(final Long visitId) {
        patientVisitRepository.deleteById(visitId);
    }

    @Transactional
    public PatientVisit update(final Long visitId, final VisitRequest request) {
        PatientVisit patientVisit = findById(visitId);

        long patientId = request.getPatientId();
        Patient patient = patientRepository.findById(patientId).orElseThrow(
                throwNotFoundException(NO_EXIST_PATIENT.formatted(patientId))
        );

        long hospitalId = request.getHospitalId();
        Hospital hospital = hospitalRepository.findById(hospitalId).orElseThrow(
                throwNotFoundException(NO_EXIST_HOSPITAL.formatted(hospitalId))
        );
        LocalDateTime receptionDateTime = request.getReceptionDateTime();

        patientVisit.update(patient, hospital, receptionDateTime);
        return patientVisit;
    }

}
