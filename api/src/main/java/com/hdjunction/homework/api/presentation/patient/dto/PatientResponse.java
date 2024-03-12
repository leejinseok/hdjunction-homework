package com.hdjunction.homework.api.presentation.patient.dto;

import com.hdjunction.homework.api.presentation.hospital.dto.HospitalResponse;
import com.hdjunction.homework.core.db.domain.hospital.Hospital;
import com.hdjunction.homework.core.db.domain.patient.Patient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class PatientResponse {

    private Long id;
    private HospitalResponse hospital;
    private String name;
    private String registrationNumber;
    private String genderCode;
    private String birth;
    private String phoneNumber;

    public static PatientResponse create(final Patient patient) {
        return PatientResponse.of(
                patient.getId(),
                HospitalResponse.create(patient.getHospital()),
                patient.getName(),
                patient.getRegistrationNumber(),
                patient.getGenderCode(),
                patient.getBirth(),
                patient.getPhoneNumber()
        );
    }
}
