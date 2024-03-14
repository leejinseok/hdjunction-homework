package com.hdjunction.homework.api.presentation.patient;

import com.hdjunction.homework.core.db.domain.common.PhoneNumber;
import com.hdjunction.homework.core.db.domain.hospital.Hospital;
import com.hdjunction.homework.core.db.domain.patient.Patient;

import java.time.LocalDateTime;
import java.util.List;

public class PatientFactory {

    public static List<Patient> getTestPatients() {
        return List.of(getTestPatient());
    }

    public static Patient getTestPatient() {
        Hospital hospital = Hospital.builder()
                .id(1L)
                .name("우리병원")
                .healthcareFacilityNumber("100")
                .hospitalDirectorName("김원장")
                .build();

        return Patient.builder()
                .id(1L)
                .name("김환자")
                .genderCode("M")
                .hospital(hospital)
                .birth("2000-10-01")
                .phoneNumber(
                        PhoneNumber.builder()
                                .number1("010")
                                .number2("1111")
                                .number3("2222")
                                .build()
                )
                .registrationNumber("20240001")
                .recentlyVisitDateTime(LocalDateTime.now())
                .build();
    }

}
