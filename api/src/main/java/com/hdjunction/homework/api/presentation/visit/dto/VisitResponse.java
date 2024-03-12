package com.hdjunction.homework.api.presentation.visit.dto;

import com.hdjunction.homework.api.presentation.hospital.dto.HospitalResponse;
import com.hdjunction.homework.api.presentation.patient.dto.PatientResponse;
import com.hdjunction.homework.core.db.domain.visit.PatientVisit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class VisitResponse {

    private long id;
    private HospitalResponse hospital;
    private PatientResponse patient;
    private LocalDateTime receptionDateTime;

    public static VisitResponse create(final PatientVisit visit) {
        return VisitResponse.of(
                visit.getId(),
                HospitalResponse.create(visit.getHospital()),
                PatientResponse.create(visit.getPatient()),
                visit.getReceptionDateTime()
        );
    }

}
