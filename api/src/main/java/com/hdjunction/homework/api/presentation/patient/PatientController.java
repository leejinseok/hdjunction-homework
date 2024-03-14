package com.hdjunction.homework.api.presentation.patient;

import com.hdjunction.homework.api.application.patient.PatientService;
import com.hdjunction.homework.api.presentation.patient.dto.PatientRequest;
import com.hdjunction.homework.api.presentation.patient.dto.PatientResponse;
import com.hdjunction.homework.core.db.domain.patient.Patient;
import com.hdjunction.homework.core.db.domain.patient.PatientSearchType;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<Page<PatientResponse>> getAllPatient(
            @Parameter(name = "pageNo", example = "0") @RequestParam(defaultValue = "0") final Integer pageNo,
            @Parameter(name = "pageSize", example = "10") @RequestParam(defaultValue = "10") final Integer pageSize,
            @Parameter(name = "searchType", example = "NAME") final PatientSearchType searchType,
            @Parameter(name = "query", example = "김환자") final String query
    ) {
        Page<Patient> all = patientService.findAll(searchType, query, PageRequest.of(pageNo, pageSize));
        Page<PatientResponse> map = all.map(PatientResponse::create);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(map);
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientResponse> getOnePatient(@PathVariable final Long patientId) {
        Patient patient = patientService.findById(patientId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PatientResponse.create(patient));
    }

    @PostMapping
    public ResponseEntity<PatientResponse> createPatient(@RequestBody final PatientRequest patientRequest) {
        Patient patient = patientService.save(patientRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(PatientResponse.create(patient));
    }

    @PatchMapping("/{patientId}")
    public ResponseEntity<PatientResponse> updatePatient(
            @PathVariable final Long patientId,
            @RequestBody final PatientRequest patientRequest
    ) {
        Patient patient = patientService.update(patientId, patientRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PatientResponse.create(patient));
    }

    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deletePatient(@PathVariable final Long patientId) {
        patientService.deleteById(patientId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
