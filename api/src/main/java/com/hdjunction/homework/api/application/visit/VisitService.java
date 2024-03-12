package com.hdjunction.homework.api.application.visit;

import com.hdjunction.homework.api.exception.NotFoundException;
import com.hdjunction.homework.api.presentation.visit.dto.VisitRequest;
import com.hdjunction.homework.core.db.domain.hospital.Hospital;
import com.hdjunction.homework.core.db.domain.hospital.HospitalRepository;
import com.hdjunction.homework.core.db.domain.patient.Patient;
import com.hdjunction.homework.core.db.domain.patient.PatientRepository;
import com.hdjunction.homework.core.db.domain.visit.PatientVisit;
import com.hdjunction.homework.core.db.domain.visit.PatientVisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final PatientVisitRepository patientVisitRepository;
    private final PatientRepository patientRepository;
    private final HospitalRepository hospitalRepository;

    public Page<PatientVisit> findPage(final Pageable pageable) {
        return patientVisitRepository.findAll(pageable);
    }

    public PatientVisit findById(final Long id) {
        return patientVisitRepository.findById(id).orElseThrow(() -> new NotFoundException("존재하지 않는 방문기록입니다."));
    }

    @Transactional
    public PatientVisit save(final VisitRequest request) {
        long patientId = request.getPatientId();
        Patient patient = patientRepository.findById(patientId).orElseThrow(() ->
                new NotFoundException("존재하지 않는 환자입니다. %s".formatted(patientId))
        );

        long hospitalId = request.getHospitalId();
        Hospital hospital = hospitalRepository.findById(hospitalId).orElseThrow(() ->
                new NotFoundException("존재하지 않는 병원입니다. %s".formatted(hospitalId))
        );

        PatientVisit visit = PatientVisit.builder()
                .patient(patient)
                .hospital(hospital)
                .receptionDateTime(request.getReceptionDateTime())
                .build();

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
        Patient patient = patientRepository.findById(patientId).orElseThrow(() ->
                new NotFoundException("존재하지 않는 환자입니다. %s".formatted(patientId))
        );
        long hospitalId = request.getHospitalId();
        Hospital hospital = hospitalRepository.findById(hospitalId).orElseThrow(() ->
                new NotFoundException("존재하지 않는 병원입니다. %s".formatted(hospitalId))
        );
        LocalDateTime receptionDateTime = request.getReceptionDateTime();

        patientVisit.update(patient, hospital, receptionDateTime);
        return patientVisit;
    }

}
