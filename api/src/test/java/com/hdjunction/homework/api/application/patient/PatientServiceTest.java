package com.hdjunction.homework.api.application.patient;

import com.hdjunction.homework.api.config.ApiDbConfig;
import com.hdjunction.homework.api.presentation.patient.dto.PatientRequest;
import com.hdjunction.homework.core.db.domain.common.PhoneNumber;
import com.hdjunction.homework.core.db.domain.hospital.Hospital;
import com.hdjunction.homework.core.db.domain.hospital.HospitalRepository;
import com.hdjunction.homework.core.db.domain.patient.Patient;
import com.hdjunction.homework.core.db.domain.patient.PatientQueryDslRepository;
import com.hdjunction.homework.core.db.domain.patient.PatientRepository;
import com.hdjunction.homework.core.db.domain.patient.PatientSearchType;
import com.hdjunction.homework.core.db.domain.visit.PatientVisit;
import com.hdjunction.homework.core.db.domain.visit.PatientVisitRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles({"test"})
@DataJpaTest
@Import({ApiDbConfig.class})
class PatientServiceTest {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    PatientQueryDslRepository patientQueryDslRepository;

    @Autowired
    PatientVisitRepository patientVisitRepository;

    @Autowired
    HospitalRepository hospitalRepository;

    long hospitalId;

    @BeforeEach
    void beforeEach() {
        Hospital hospital = hospitalRepository.save(
                Hospital.builder()
                        .name("우리병원")
                        .healthcareFacilityNumber("100")
                        .hospitalDirectorName("김원장")
                        .build()
        );

        hospitalId = hospital.getId();
    }

    @AfterEach
    void afterAll() {
        hospitalRepository.deleteAll();
    }

    @DisplayName("환자등록 테스트")
    @Test
    void save() {
        PatientService patientService = createService();

        PatientRequest request = new PatientRequest();
        request.setHospitalId(hospitalId);
        request.setName("김환자");
        request.setBirth("2000-01-01");
        request.setGenderCode("M");
        request.setPhoneNumber(new PhoneNumber("010", "1111", "2222"));

        Patient saved = patientService.save(request);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo(request.getName());
        assertThat(saved.getBirth()).isEqualTo(request.getBirth());
        assertThat(saved.getGenderCode()).isEqualTo(request.getGenderCode());
    }

    @DisplayName("환자등록번호 생성 테스트")
    @Test
    void generateRegistrationNumber() {
        PatientService patientService = createService();
        PatientRequest request = new PatientRequest();
        request.setHospitalId(hospitalId);
        request.setName("김환자");
        request.setBirth("2000-01-01");
        request.setGenderCode("M");
        request.setPhoneNumber(new PhoneNumber("010", "1111", "2222"));
        Patient save = patientService.save(request);
        int savedRegistrationNumber = Integer.parseInt(
                save.getRegistrationNumber().split("")[save.getRegistrationNumber().length() - 1]
        );

        // 새로 등록 되는 환자는 이전 환자번호보다 마지막 숫자가 1 더 커야 한다
        String registrationNumber = patientService.generateRegistrationNumber(hospitalId);
        String lastStr = registrationNumber.split("")[registrationNumber.length() - 1];
        int lastNum = Integer.parseInt(lastStr);

        assertThat(savedRegistrationNumber + 1 == lastNum).isTrue();
    }

    @DisplayName("환자정보 수정 테스트")
    @Test
    void update() {
        PatientService patientService = createService();

        PatientRequest saveRequest = new PatientRequest();
        saveRequest.setHospitalId(hospitalId);
        saveRequest.setName("김환자");
        saveRequest.setBirth("2000-01-01");
        saveRequest.setGenderCode("M");
        saveRequest.setPhoneNumber(new PhoneNumber("010", "1111", "2222"));
        Patient save = patientService.save(saveRequest);

        Long id = save.getId();
        String registrationNumber = save.getRegistrationNumber();
        String name = save.getName();
        String birth = save.getBirth();
        String genderCode = save.getGenderCode();
        PhoneNumber phoneNumber = save.getPhoneNumber();

        PatientRequest updateRequest = new PatientRequest();
        updateRequest.setHospitalId(hospitalId);
        updateRequest.setName("박환자");
        updateRequest.setBirth("2000-02-02");
        updateRequest.setGenderCode("F");
        updateRequest.setPhoneNumber(new PhoneNumber("010", "2222", "1111"));
        Patient update = patientService.update(save.getId(), updateRequest);

        assertThat(id).isEqualTo(update.getId());
        assertThat(registrationNumber).isEqualTo(update.getRegistrationNumber());
        assertThat(name).isNotEqualTo(update.getName());
        assertThat(birth).isNotEqualTo(update.getBirth());
        assertThat(genderCode).isNotEqualTo(update.getGenderCode());
        assertThat(phoneNumber).isNotEqualTo(update.getPhoneNumber());
    }

    @DisplayName("환자삭제 테스트")
    @Test
    void deleteById() {
        PatientService patientService = createService();
        PatientRequest saveRequest = new PatientRequest();
        saveRequest.setHospitalId(hospitalId);
        saveRequest.setName("김환자");
        saveRequest.setBirth("2000-01-01");
        saveRequest.setGenderCode("M");
        saveRequest.setPhoneNumber(new PhoneNumber("010", "1111", "2222"));
        Patient patient = patientService.save(saveRequest);

        Hospital hospital = hospitalRepository.findById(hospitalId).orElseThrow(() -> new RuntimeException("실패"));
        PatientVisit visit = PatientVisit.builder()
                .receptionDateTime(LocalDateTime.now())
                .patient(patient)
                .hospital(hospital)
                .build();

        patientVisitRepository.save(visit);
        patientService.deleteById(patient.getId());

        List<PatientVisit> visits = patientVisitRepository.findAllByPatientId(patient.getId());
        assertThat(visits.size()).isEqualTo(0);
    }

    @DisplayName("환자조회 (단건) 테스트")
    @Test
    void findById() {
        PatientService patientService = createService();

        PatientRequest saveRequest = new PatientRequest();
        saveRequest.setHospitalId(hospitalId);
        saveRequest.setName("김환자");
        saveRequest.setBirth("2000-01-01");
        saveRequest.setGenderCode("M");
        saveRequest.setPhoneNumber(new PhoneNumber("010", "1111", "2222"));
        Patient saved = patientService.save(saveRequest);

        Patient patient = patientService.findById(saved.getId());

        assertThat(saved.getId()).isEqualTo(patient.getId());
        assertThat(saved.getName()).isEqualTo(patient.getName());
        assertThat(saved.getBirth()).isEqualTo(patient.getBirth());
        assertThat(saved.getGenderCode()).isEqualTo(patient.getGenderCode());
        assertThat(saved.getPhoneNumber()).isEqualTo(patient.getPhoneNumber());
    }

    @DisplayName("환자조회 (리스트) 테스트")
    @Test
    void findAll() {
        PatientService patientService = createService();
        PatientRequest saveRequest = new PatientRequest();
        saveRequest.setHospitalId(hospitalId);
        saveRequest.setName("김환자");
        saveRequest.setBirth("2000-01-01");
        saveRequest.setGenderCode("M");
        saveRequest.setPhoneNumber(new PhoneNumber("010", "1111", "2222"));
        Patient saved = patientService.save(saveRequest);

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Patient> page = patientService.findAll(PatientSearchType.NAME, "김환자", pageRequest);

        assertThat(page.getSize()).isEqualTo(pageRequest.getPageSize());
        assertThat(page.getNumber()).isEqualTo(pageRequest.getPageNumber());
        Patient patient = page.getContent().get(0);

        assertThat(patient.getId()).isEqualTo(saved.getId());
    }

    PatientService createService() {
        return new PatientService(patientRepository, patientQueryDslRepository, patientVisitRepository, hospitalRepository);
    }

}