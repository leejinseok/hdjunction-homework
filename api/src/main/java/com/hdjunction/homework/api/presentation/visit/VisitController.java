package com.hdjunction.homework.api.presentation.visit;

import com.hdjunction.homework.api.application.visit.VisitService;
import com.hdjunction.homework.api.presentation.visit.dto.VisitRequest;
import com.hdjunction.homework.api.presentation.visit.dto.VisitResponse;
import com.hdjunction.homework.core.db.domain.visit.PatientVisit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/visits")
public class VisitController {

    private final VisitService visitService;

    @GetMapping
    public ResponseEntity<Page<VisitResponse>> getVisits(@PageableDefault(size = 20, sort = "receptionDateTime,DESC") final Pageable pageable) {
        Page<PatientVisit> visitsPage = visitService.findPage(pageable);
        Page<VisitResponse> map = visitsPage.map(VisitResponse::create);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(map);
    }

    @GetMapping("/{visitId}")
    public ResponseEntity<VisitResponse> getVisit(@PathVariable final Long visitId) {
        PatientVisit patientVisit = visitService.findById(visitId);
        VisitResponse response = VisitResponse.create(patientVisit);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping
    public ResponseEntity<VisitResponse> saveVisit(@RequestBody final VisitRequest visitRequest) {
        PatientVisit save = visitService.save(visitRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(VisitResponse.create(save));
    }

    @PatchMapping("/{visitId}")
    public ResponseEntity<VisitResponse> updateVisit(
            @PathVariable final Long visitId,
            @RequestBody final VisitRequest request
    ) {
        PatientVisit update = visitService.update(visitId, request);
        return ResponseEntity.status(HttpStatus.OK).body(
                VisitResponse.create(update)
        );
    }

    @DeleteMapping("/{visitId}")
    public ResponseEntity<Void> deleteVisit(@PathVariable final Long visitId) {
        visitService.deleteById(visitId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
