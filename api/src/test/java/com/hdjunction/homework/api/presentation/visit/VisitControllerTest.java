package com.hdjunction.homework.api.presentation.visit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdjunction.homework.api.application.visit.VisitService;
import com.hdjunction.homework.api.config.ApiSecurityConfig;
import com.hdjunction.homework.api.presentation.visit.dto.VisitRequest;
import com.hdjunction.homework.core.db.domain.visit.PatientVisit;
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

import java.time.LocalDateTime;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {VisitController.class})
@ExtendWith(RestDocumentationExtension.class)
@Import({ApiSecurityConfig.class})
class VisitControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    VisitService visitService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }


    @DisplayName("환자 방문 조회 (페이징) 테스트")
    @Test
    void getVisits() throws Exception {
        List<PatientVisit> testPatientVisits = VisitFactory.getTestPatientVisits();
        Page<PatientVisit> page = new PageImpl<>(testPatientVisits);
        when(visitService.findPage(any(), any(), any())).thenReturn(page);

        this.mockMvc.perform(get("/api/v1/visits"))
                .andDo(document(
                        "get_visits",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("pageNo").description("페이지 번호").optional(),
                                parameterWithName("pageSize").description("페이지 size").optional(),
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
                                fieldWithPath("content.[].patient").type(OBJECT).description("환자"),
                                fieldWithPath("content.[].patient.id").type(NUMBER).description("환자 id"),
                                fieldWithPath("content.[].patient.name").type(STRING).description("환자명"),
                                fieldWithPath("content.[].patient.registrationNumber").type(STRING).description("환자등록번호"),
                                fieldWithPath("content.[].patient.genderCode").type(STRING).description("환자성별코드"),
                                fieldWithPath("content.[].patient.birth").type(STRING).description("환자생년월일"),
                                fieldWithPath("content.[].patient.phoneNumber").type(STRING).description("환자전화번호"),
                                fieldWithPath("content.[].receptionDateTime").type(STRING).description("환자방문일")
                        )
                ))
                .andDo(print());
    }

    @DisplayName("환자 방문 단건 조회")
    @Test
    void getVisit() throws Exception {
        PatientVisit visit = VisitFactory.getTestPatientVisit();
        when(visitService.findById(any())).thenReturn(visit);

        this.mockMvc.perform(get("/api/v1/visits/{visitId}", 1))
                .andDo(document(
                        "get_visit",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("visitId").description("방문 id")
                        ),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("id"),
                                fieldWithPath("hospital").type(OBJECT).description("병원"),
                                fieldWithPath("hospital.id").type(NUMBER).description("병원 id"),
                                fieldWithPath("hospital.name").type(STRING).description("병원명"),
                                fieldWithPath("hospital.healthcareFacilityNumber").type(STRING).description("요양기관번호"),
                                fieldWithPath("hospital.hospitalDirectorName").type(STRING).description("병원장이름"),
                                fieldWithPath("patient").type(OBJECT).description("환자"),
                                fieldWithPath("patient.id").type(NUMBER).description("환자 id"),
                                fieldWithPath("patient.name").type(STRING).description("환자명"),
                                fieldWithPath("patient.registrationNumber").type(STRING).description("환자등록번호"),
                                fieldWithPath("patient.genderCode").type(STRING).description("환자성별코드"),
                                fieldWithPath("patient.birth").type(STRING).description("환자생년월일"),
                                fieldWithPath("patient.phoneNumber").type(STRING).description("환자전화번호"),
                                fieldWithPath("receptionDateTime").type(STRING).description("환자방문일")
                        )
                ))
                .andDo(print());
    }

    @DisplayName("환자 방문정보 저장")
    @Test
    void saveVisit() throws Exception {
        VisitRequest visitRequest = VisitRequest.of(1L, 1L, LocalDateTime.now());
        String valueAsString = objectMapper.writeValueAsString(visitRequest);

        PatientVisit visit = VisitFactory.getTestPatientVisit();
        when(visitService.save(any())).thenReturn(visit);

        this.mockMvc.perform(
                        post("/api/v1/visits")
                                .content(valueAsString)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(document(
                        "create_visit",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("hospitalId").type(NUMBER).description("병원 id"),
                                fieldWithPath("patientId").type(NUMBER).description("환자 id"),
                                fieldWithPath("receptionDateTime").type(STRING).description("방문일시")
                        ),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("id"),
                                fieldWithPath("hospital").type(OBJECT).description("병원"),
                                fieldWithPath("hospital.id").type(NUMBER).description("병원 id"),
                                fieldWithPath("hospital.name").type(STRING).description("병원명"),
                                fieldWithPath("hospital.healthcareFacilityNumber").type(STRING).description("요양기관번호"),
                                fieldWithPath("hospital.hospitalDirectorName").type(STRING).description("병원장이름"),
                                fieldWithPath("patient").type(OBJECT).description("환자"),
                                fieldWithPath("patient.id").type(NUMBER).description("환자 id"),
                                fieldWithPath("patient.name").type(STRING).description("환자명"),
                                fieldWithPath("patient.registrationNumber").type(STRING).description("환자등록번호"),
                                fieldWithPath("patient.genderCode").type(STRING).description("환자성별코드"),
                                fieldWithPath("patient.birth").type(STRING).description("환자생년월일"),
                                fieldWithPath("patient.phoneNumber").type(STRING).description("환자전화번호"),
                                fieldWithPath("receptionDateTime").type(STRING).description("환자방문일")
                        )
                ))
                .andDo(print());
    }

    @DisplayName("환자 방문정보 수정")
    @Test
    void updateVisit() throws Exception {
        VisitRequest visitRequest = VisitRequest.of(1L, 1L, LocalDateTime.now());
        String valueAsString = objectMapper.writeValueAsString(visitRequest);

        PatientVisit visit = VisitFactory.getTestPatientVisit();
        when(visitService.update(any(), any())).thenReturn(visit);

        this.mockMvc.perform(
                        patch("/api/v1/visits/{visitId}", 1)
                                .content(valueAsString)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(document(
                        "update_visit",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("visitId").description("방문정보 id")
                        ),
                        requestFields(
                                fieldWithPath("hospitalId").type(NUMBER).description("병원 id"),
                                fieldWithPath("patientId").type(NUMBER).description("환자 id"),
                                fieldWithPath("receptionDateTime").type(STRING).description("방문일시")
                        ),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("id"),
                                fieldWithPath("hospital").type(OBJECT).description("병원"),
                                fieldWithPath("hospital.id").type(NUMBER).description("병원 id"),
                                fieldWithPath("hospital.name").type(STRING).description("병원명"),
                                fieldWithPath("hospital.healthcareFacilityNumber").type(STRING).description("요양기관번호"),
                                fieldWithPath("hospital.hospitalDirectorName").type(STRING).description("병원장이름"),
                                fieldWithPath("patient").type(OBJECT).description("환자"),
                                fieldWithPath("patient.id").type(NUMBER).description("환자 id"),
                                fieldWithPath("patient.name").type(STRING).description("환자명"),
                                fieldWithPath("patient.registrationNumber").type(STRING).description("환자등록번호"),
                                fieldWithPath("patient.genderCode").type(STRING).description("환자성별코드"),
                                fieldWithPath("patient.birth").type(STRING).description("환자생년월일"),
                                fieldWithPath("patient.phoneNumber").type(STRING).description("환자전화번호"),
                                fieldWithPath("receptionDateTime").type(STRING).description("환자방문일")
                        )
                ))
                .andDo(print());
    }

    @DisplayName("환자방문정보 삭제")
    @Test
    void deleteVisit() throws Exception {
        doNothing().when(visitService).deleteById(anyLong());

        this.mockMvc.perform(
                        delete("/api/v1/visits/{visitId}", 1)
                )
                .andDo(
                        document(
                                "delete_visit",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("visitId").description("환자방문 id")
                                )
                        )
                )
                .andExpect(status().isNoContent());
    }
}