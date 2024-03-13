package com.hdjunction.homework.api;

import com.hdjunction.homework.core.db.domain.code.Code;
import com.hdjunction.homework.core.db.domain.code.CodeGroup;
import com.hdjunction.homework.core.db.domain.code.CodeGroupRepository;
import com.hdjunction.homework.core.db.domain.code.CodeRepository;
import com.hdjunction.homework.core.db.domain.common.PhoneNumber;
import com.hdjunction.homework.core.db.domain.hospital.Hospital;
import com.hdjunction.homework.core.db.domain.hospital.HospitalRepository;
import com.hdjunction.homework.core.db.domain.patient.Patient;
import com.hdjunction.homework.core.db.domain.patient.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ApiApplicationRunner implements ApplicationRunner {

    private final HospitalRepository hospitalRepository;
    private final PatientRepository patientRepository;
    private final CodeGroupRepository codeGroupRepository;
    private final CodeRepository codeRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        makeCodeGroup();

        Hospital hospital = hospitalRepository.save(
                Hospital.builder()
                        .name("우리병원")
                        .healthcareFacilityNumber("100")
                        .hospitalDirectorName("김원장")
                        .build()
        );

        Patient patient = Patient.builder()
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

        patientRepository.save(patient);
    }

    private void makeCodeGroup() {
        CodeGroup codeGroup1 = CodeGroup.builder()
                .codeGroup("성별코드")
                .name("성별코드")
                .description("성별을 표시")
                .build();
        codeGroupRepository.save(codeGroup1);
        codeRepository.save(
                Code.builder()
                        .codeGroup(codeGroup1)
                        .code("M")
                        .name("남")
                        .build()
        );
        codeRepository.save(
                Code.builder()
                        .codeGroup(codeGroup1)
                        .code("F")
                        .name("여")
                        .build()
        );

        CodeGroup codeGroup2 = CodeGroup.builder()
                .codeGroup("방문상태코드")
                .name("방문상태코드")
                .description("환자방문의 상태(방문증, 종료, 취소)")
                .build();
        codeGroupRepository.save(codeGroup2);
        codeRepository.save(
                Code.builder()
                        .codeGroup(codeGroup2)
                        .code("1")
                        .name("방문증")
                        .build()
        );
        codeRepository.save(
                Code.builder()
                        .codeGroup(codeGroup2)
                        .code("2")
                        .name("종료")
                        .build()
        );
        codeRepository.save(
                Code.builder()
                        .codeGroup(codeGroup2)
                        .code("3")
                        .name("취소")
                        .build()
        );

        CodeGroup codeGroup3 = CodeGroup.builder()
                .codeGroup("진료과목코드")
                .name("진료과목코드")
                .description("진료과목 (내과, 안과 등)")
                .build();
        codeGroupRepository.save(codeGroup3);
        codeRepository.save(
                Code.builder()
                        .codeGroup(codeGroup3)
                        .code("01")
                        .name("내과")
                        .build()
        );
        codeRepository.save(
                Code.builder()
                        .codeGroup(codeGroup3)
                        .code("02")
                        .name("안과")
                        .build()
        );

        CodeGroup codeGroup4 = CodeGroup.builder()
                .codeGroup("진료유형코드")
                .name("진료유형코드")
                .description("진료의 유형(약처방, 검사 등)")
                .build();
        codeGroupRepository.save(codeGroup4);
        codeRepository.save(
                Code.builder()
                        .codeGroup(codeGroup4)
                        .code("D")
                        .name("약처방")
                        .build()
        );
        codeRepository.save(
                Code.builder()
                        .codeGroup(codeGroup4)
                        .code("T")
                        .name("검사")
                        .build()
        );
    }
}
