package com.hdjunction.homework.api.application.patient;

import com.hdjunction.homework.api.exception.NotFoundException;
import com.hdjunction.homework.api.presentation.patient.dto.PatientRequest;
import com.hdjunction.homework.core.db.domain.hospital.Hospital;
import com.hdjunction.homework.core.db.domain.hospital.HospitalRepository;
import com.hdjunction.homework.core.db.domain.patient.Patient;
import com.hdjunction.homework.core.db.domain.patient.PatientQueryDslRepository;
import com.hdjunction.homework.core.db.domain.patient.PatientRepository;
import com.hdjunction.homework.core.db.domain.patient.PatientSearchType;
import com.hdjunction.homework.core.db.domain.visit.PatientVisit;
import com.hdjunction.homework.core.db.domain.visit.PatientVisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientQueryDslRepository patientQueryDslRepository;
    private final PatientVisitRepository patientVisitRepository;
    private final HospitalRepository hospitalRepository;


    public Page<Patient> findAll(final PatientSearchType searchType, final String query, final Pageable pageable) {
        return patientQueryDslRepository.findAll(searchType, query, pageable);
    }

    public Patient findById(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 환자입니다. [id=%s]".formatted(patientId)));
    }

    @Transactional
    public Patient save(final PatientRequest patientRequest) {
        long hospitalId = patientRequest.getHospitalId();
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 병원입니다. [id=%s]".formatted(hospitalId)));

        Patient build = Patient.builder()
                .name(patientRequest.getName())
                .hospital(hospital)
                .registrationNumber(generateRegistrationNumber(hospitalId))
                .birth(patientRequest.getBirth())
                .genderCode(patientRequest.getGenderCode())
                .phoneNumber(patientRequest.getPhoneNumber())
                .build();

        return patientRepository.save(build);
    }

    public String generateRegistrationNumber(final long hospitalId) {
        Long count = patientRepository.countByHospitalId(hospitalId);
        return String.format("%s%04d", LocalDateTime.now().getYear(), count + 1);
    }

    @Transactional
    public Patient update(final long patientId, final PatientRequest patientRequest) {
        Patient patient = findById(patientId);
        long hospitalId = patientRequest.getHospitalId();

        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 병원입니다. [id=%s]".formatted(hospitalId)));

        patient.update(
                hospital,
                patientRequest.getName(),
                patientRequest.getGenderCode(),
                patientRequest.getBirth(),
                patientRequest.getPhoneNumber()
        );

        return patient;
    }


    @Transactional
    public void deleteById(final Long patientId) {
        Patient patient = findById(patientId);
        List<PatientVisit> visits = patientVisitRepository.findAllByPatientId(patientId);
        patientVisitRepository.deleteAll(visits);
        patientRepository.delete(patient);
    }

}
