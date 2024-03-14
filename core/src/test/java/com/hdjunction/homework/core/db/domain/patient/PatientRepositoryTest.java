package com.hdjunction.homework.core.db.domain.patient;

import com.hdjunction.homework.core.CoreTestConfiguration;
import com.hdjunction.homework.core.db.config.CoreDbConfig;
import com.hdjunction.homework.core.db.domain.common.PhoneNumber;
import com.hdjunction.homework.core.db.domain.hospital.Hospital;
import com.hdjunction.homework.core.db.domain.hospital.HospitalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertThrows;


@ActiveProfiles({"test"})
@DataJpaTest
@ContextConfiguration(classes = CoreTestConfiguration.class)
@Import({CoreDbConfig.class})
class PatientRepositoryTest {

    @Autowired
    HospitalRepository hospitalRepository;

    @Autowired
    PatientRepository patientRepository;

    @DisplayName("동일 환자등록번호 저장 테스트")
    @Test
    void saveSameRegistrationNumber() {
        Hospital hospital = hospitalRepository.save(
                Hospital.builder()
                        .name("우리병원")
                        .healthcareFacilityNumber("100")
                        .hospitalDirectorName("김원장")
                        .build()
        );

        Patient patient1 = Patient.builder()
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
                .build();

        patientRepository.save(patient1);

        assertThrows(DataIntegrityViolationException.class, () -> {
            Patient patient2 = Patient.builder()
                    .name("박환자")
                    .genderCode("F")
                    .hospital(hospital)
                    .birth("2000-10-01")
                    .phoneNumber(
                            PhoneNumber.builder()
                                    .number1("010")
                                    .number2("2222")
                                    .number3("1111")
                                    .build()
                    )
                    .registrationNumber("20240001")
                    .build();
            patientRepository.save(patient2);

        });
    }

}