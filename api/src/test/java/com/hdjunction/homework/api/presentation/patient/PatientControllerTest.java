package com.hdjunction.homework.api.presentation.patient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdjunction.homework.api.application.patient.PatientService;
import com.hdjunction.homework.api.config.ApiSecurityConfig;
import com.hdjunction.homework.api.presentation.patient.dto.PatientRequest;
import com.hdjunction.homework.core.db.domain.common.PhoneNumber;
import com.hdjunction.homework.core.db.domain.patient.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = {PatientController.class})
@ExtendWith(RestDocumentationExtension.class)
@Import({ApiSecurityConfig.class})
class PatientControllerTest {


    @Autowired
    MockMvc mockMvc;

    @MockBean
    PatientService patientService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @DisplayName("환자목록 페이지조회 테스트")
    @Test
    void getAllPatient() throws Exception {
        List<Patient> testPatients = PatientFactory.getTestPatients();
        Page<Patient> page = new PageImpl<>(testPatients);

        when(patientService.findAll(any(), any(), any())).thenReturn(page);
        this.mockMvc.perform(
                        get("/api/v1/patients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(
                        document(
                                "get_patients",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("page").description("페이지 번호").optional(),
                                        parameterWithName("size").description("페이지 size").optional(),
                                        parameterWithName("searchType").description("검색 유형").optional(),
                                        parameterWithName("query").description("검색어").optional()
                                ),
                                responseFields(
                                        fieldWithPath("pageable").type(STRING).description("페이징"),
                                        fieldWithPath("last").type(BOOLEAN).description("마지막 페이지 여부"),
                                        fieldWithPath("totalPages").type(NUMBER).description("전체 페이지 수"),
                                        fieldWithPath("totalElements").type(NUMBER).description("전체 아티스트 수"),
                                        fieldWithPath("number").type(NUMBER).description("페이지 number"),
                                        fieldWithPath("first").type(BOOLEAN).description("첫번째 페이지 여부"),
                                        fieldWithPath("sort").type(OBJECT).description("페이징 정렬"),
                                        fieldWithPath("sort.sorted").type(BOOLEAN).description("sorted"),
                                        fieldWithPath("sort.unsorted").type(BOOLEAN).description("unsorted"),
                                        fieldWithPath("sort.empty").type(BOOLEAN).description("empty"),
                                        fieldWithPath("size").type(NUMBER).description("페이지 size"),
                                        fieldWithPath("numberOfElements").type(NUMBER).description("numberOfElements"),
                                        fieldWithPath("empty").type(BOOLEAN).description("empty"),
                                        fieldWithPath("content").type(ARRAY).description("content"),
                                        fieldWithPath("content.[].id").type(NUMBER).description("id"),
                                        fieldWithPath("content.[].hospital").type(OBJECT).description("병원"),
                                        fieldWithPath("content.[].hospital.id").type(NUMBER).description("병원 id"),
                                        fieldWithPath("content.[].hospital.name").type(STRING).description("병원명"),
                                        fieldWithPath("content.[].hospital.healthcareFacilityNumber").type(STRING).description("요양기관번호"),
                                        fieldWithPath("content.[].hospital.hospitalDirectorName").type(STRING).description("병원장이름"),
                                        fieldWithPath("content.[].name").type(STRING).description("환자이름"),
                                        fieldWithPath("content.[].registrationNumber").type(STRING).description("환자등록번호"),
                                        fieldWithPath("content.[].genderCode").type(STRING).description("환자성별코드"),
                                        fieldWithPath("content.[].birth").type(STRING).description("환자생년월일"),
                                        fieldWithPath("content.[].phoneNumber").type(STRING).description("환자전화번호")
                                )
                        )
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageable").exists())
                .andExpect(jsonPath("$.last").exists())
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.first").exists())
                .andExpect(jsonPath("$.sort").exists())
                .andExpect(jsonPath("$.sort.sorted").exists())
                .andExpect(jsonPath("$.sort.unsorted").exists())
                .andExpect(jsonPath("$.sort.empty").exists())
                .andExpect(jsonPath("$.size").exists())
                .andExpect(jsonPath("$.numberOfElements").exists())
                .andExpect(jsonPath("$.empty").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.content[0].hospital").exists())
                .andExpect(jsonPath("$.content[0].hospital.id").exists())
                .andExpect(jsonPath("$.content[0].hospital.name").exists())
                .andExpect(jsonPath("$.content[0].hospital.healthcareFacilityNumber").exists())
                .andExpect(jsonPath("$.content[0].hospital.hospitalDirectorName").exists())
                .andExpect(jsonPath("$.content[0].name").exists())
                .andExpect(jsonPath("$.content[0].registrationNumber").exists())
                .andExpect(jsonPath("$.content[0].genderCode").exists())
                .andExpect(jsonPath("$.content[0].birth").exists())
                .andExpect(jsonPath("$.content[0].phoneNumber").exists());
    }

    @DisplayName("환자 단건조회 테스트")
    @Test
    void getOnePatient() throws Exception {
        Patient testPatient = PatientFactory.getTestPatient();
        when(patientService.findById(any())).thenReturn(
                testPatient
        );

        this.mockMvc.perform(get("/api/v1/patients/{patientId}", 1))
                .andDo(
                        document(
                                "get_patient_by_id",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("patientId").description("환자 id")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("id"),
                                        fieldWithPath("name").type(STRING).description("이름"),
                                        fieldWithPath("hospital").type(OBJECT).description("병원명"),
                                        fieldWithPath("hospital.id").type(NUMBER).description("병원 id"),
                                        fieldWithPath("hospital.name").type(STRING).description("병원명"),
                                        fieldWithPath("hospital.healthcareFacilityNumber").type(STRING).description("요양기관번호"),
                                        fieldWithPath("hospital.hospitalDirectorName").type(STRING).description("병원장이름"),
                                        fieldWithPath("registrationNumber").type(STRING).description("환자등록번호"),
                                        fieldWithPath("genderCode").type(STRING).description("환자성별코드"),
                                        fieldWithPath("birth").type(STRING).description("환자생년월일"),
                                        fieldWithPath("phoneNumber").type(STRING).description("환자연락처")
                                )
                        )
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testPatient.getId()))
                .andExpect(jsonPath("$.hospital").exists())
                .andExpect(jsonPath("$.hospital.id").value(testPatient.getHospital().getId()))
                .andExpect(jsonPath("$.hospital.name").value(testPatient.getHospital().getName()))
                .andExpect(jsonPath("$.hospital.healthcareFacilityNumber").value(testPatient.getHospital().getHealthcareFacilityNumber()))
                .andExpect(jsonPath("$.name").value(testPatient.getName()))
                .andExpect(jsonPath("$.registrationNumber").value(testPatient.getRegistrationNumber()))
                .andExpect(jsonPath("$.genderCode").value(testPatient.getGenderCode()))
                .andExpect(jsonPath("$.birth").value(testPatient.getBirth()))
                .andExpect(jsonPath("$.phoneNumber").value(testPatient.getPhoneNumber().format()));
    }

    @DisplayName("환자등록 테스트")
    @Test
    void createPatient() throws Exception {
        Patient testPatient = PatientFactory.getTestPatient();
        when(patientService.save(any())).thenReturn(
                testPatient
        );
        PhoneNumber phone = PhoneNumber.builder()
                .number1("010")
                .number2("1111")
                .number3("2222")
                .build();
        PatientRequest request = PatientRequest.of(1L, "김환자", "M", "2000-01-01", phone);
        String content = objectMapper.writeValueAsString(request);

        this.mockMvc.perform(
                        post("/api/v1/patients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                ).andDo(
                        document(
                                "create_patient",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("hospitalId").type(NUMBER).description("병원 id"),
                                        fieldWithPath("name").type(STRING).description("환자명"),
                                        fieldWithPath("genderCode").type(STRING).description("환자성별코드"),
                                        fieldWithPath("birth").type(STRING).description("생년월일").optional(),
                                        fieldWithPath("phoneNumber").type(OBJECT).description("핸드폰번호").optional(),
                                        fieldWithPath("phoneNumber.number1").type(STRING).description("핸드폰번호 1").optional(),
                                        fieldWithPath("phoneNumber.number2").type(STRING).description("핸드폰번호 2").optional(),
                                        fieldWithPath("phoneNumber.number3").type(STRING).description("핸드폰번호 3").optional()
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("id"),
                                        fieldWithPath("name").type(STRING).description("이름"),
                                        fieldWithPath("hospital").type(OBJECT).description("병원명"),
                                        fieldWithPath("hospital.id").type(NUMBER).description("병원 id"),
                                        fieldWithPath("hospital.name").type(STRING).description("병원명"),
                                        fieldWithPath("hospital.healthcareFacilityNumber").type(STRING).description("요양기관번호"),
                                        fieldWithPath("hospital.hospitalDirectorName").type(STRING).description("병원장이름"),
                                        fieldWithPath("registrationNumber").type(STRING).description("환자등록번호"),
                                        fieldWithPath("genderCode").type(STRING).description("환자성별코드"),
                                        fieldWithPath("birth").type(STRING).description("환자생년월일"),
                                        fieldWithPath("phoneNumber").type(STRING).description("환자연락처")
                                )
                        )
                )
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(testPatient.getId()))
                .andExpect(jsonPath("$.hospital").exists())
                .andExpect(jsonPath("$.hospital.id").value(testPatient.getHospital().getId()))
                .andExpect(jsonPath("$.hospital.name").value(testPatient.getHospital().getName()))
                .andExpect(jsonPath("$.hospital.healthcareFacilityNumber").value(testPatient.getHospital().getHealthcareFacilityNumber()))
                .andExpect(jsonPath("$.name").value(testPatient.getName()))
                .andExpect(jsonPath("$.registrationNumber").value(testPatient.getRegistrationNumber()))
                .andExpect(jsonPath("$.genderCode").value(testPatient.getGenderCode()))
                .andExpect(jsonPath("$.birth").value(testPatient.getBirth()))
                .andExpect(jsonPath("$.phoneNumber").value(testPatient.getPhoneNumber().format()));
    }

    @DisplayName("환자정보 업데이트 테스트")
    @Test
    void updatePatient() throws Exception {
        Patient testPatient = PatientFactory.getTestPatient();
        when(patientService.update(anyLong(), any())).thenReturn(
                testPatient
        );

        PhoneNumber phone = PhoneNumber.builder()
                .number1("010")
                .number2("1111")
                .number3("2222")
                .build();
        PatientRequest request = PatientRequest.of(1L, "김환자", "M", "2000-01-01", phone);
        String content = objectMapper.writeValueAsString(request);

        this.mockMvc.perform(
                        patch("/api/v1/patients/{patientId}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                ).andDo(
                        document(
                                "update_patient",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("patientId").description("환자 id")
                                ),
                                requestFields(
                                        fieldWithPath("hospitalId").type(NUMBER).description("병원 id").optional(),
                                        fieldWithPath("name").type(STRING).description("환자명").optional(),
                                        fieldWithPath("genderCode").type(STRING).description("환자성별코드").optional(),
                                        fieldWithPath("birth").type(STRING).description("생년월일").optional(),
                                        fieldWithPath("phoneNumber").type(OBJECT).description("핸드폰번호").optional(),
                                        fieldWithPath("phoneNumber.number1").type(STRING).description("핸드폰번호 1").optional(),
                                        fieldWithPath("phoneNumber.number2").type(STRING).description("핸드폰번호 2").optional(),
                                        fieldWithPath("phoneNumber.number3").type(STRING).description("핸드폰번호 3").optional()
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("id"),
                                        fieldWithPath("name").type(STRING).description("이름"),
                                        fieldWithPath("hospital").type(OBJECT).description("병원명"),
                                        fieldWithPath("hospital.id").type(NUMBER).description("병원 id"),
                                        fieldWithPath("hospital.name").type(STRING).description("병원명"),
                                        fieldWithPath("hospital.healthcareFacilityNumber").type(STRING).description("요양기관번호"),
                                        fieldWithPath("hospital.hospitalDirectorName").type(STRING).description("병원장이름"),
                                        fieldWithPath("registrationNumber").type(STRING).description("환자등록번호"),
                                        fieldWithPath("genderCode").type(STRING).description("환자성별코드"),
                                        fieldWithPath("birth").type(STRING).description("환자생년월일"),
                                        fieldWithPath("phoneNumber").type(STRING).description("환자연락처")
                                )
                        )
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(testPatient.getId()))
                .andExpect(jsonPath("$.hospital").exists())
                .andExpect(jsonPath("$.hospital.id").value(testPatient.getHospital().getId()))
                .andExpect(jsonPath("$.hospital.name").value(testPatient.getHospital().getName()))
                .andExpect(jsonPath("$.hospital.healthcareFacilityNumber").value(testPatient.getHospital().getHealthcareFacilityNumber()))
                .andExpect(jsonPath("$.name").value(testPatient.getName()))
                .andExpect(jsonPath("$.registrationNumber").value(testPatient.getRegistrationNumber()))
                .andExpect(jsonPath("$.genderCode").value(testPatient.getGenderCode()))
                .andExpect(jsonPath("$.birth").value(testPatient.getBirth()))
                .andExpect(jsonPath("$.phoneNumber").value(testPatient.getPhoneNumber().format()));

    }

    @DisplayName("환자삭제 테스트")
    @Test
    void deletePatient() throws Exception {
        doNothing().when(patientService).deleteById(anyLong());
        this.mockMvc.perform(
                        delete("/api/v1/patients/{patientId}", 1)
                )
                .andDo(document(
                        "delete_patient",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("patientId").description("환자 id")
                        )
                ))
                .andExpect(status().isNoContent());
    }
}