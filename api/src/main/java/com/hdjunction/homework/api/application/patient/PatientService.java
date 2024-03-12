package com.hdjunction.homework.api.application.patient;

import com.hdjunction.homework.api.exception.NotFoundException;
import com.hdjunction.homework.api.presentation.patient.dto.PatientRequest;
import com.hdjunction.homework.core.db.domain.hospital.Hospital;
import com.hdjunction.homework.core.db.domain.hospital.HospitalRepository;
import com.hdjunction.homework.core.db.domain.patient.Patient;
import com.hdjunction.homework.core.db.domain.patient.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final HospitalRepository hospitalRepository;
    private final PatientRepository patientRepository;

    public Page<Patient> findAll(final Pageable pageable) {
        return patientRepository.findAll(pageable);
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
                .registrationNumber(patientRequest.getRegistrationNumber())
                .birth(patientRequest.getBirth())
                .phoneNumber(patientRequest.getPhoneNumber())
                .build();

        return patientRepository.save(build);
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
                patientRequest.getRegistrationNumber(),
                patientRequest.getGenderCode(),
                patientRequest.getBirth(),
                patientRequest.getPhoneNumber()
        );

        return patient;
    }
}
