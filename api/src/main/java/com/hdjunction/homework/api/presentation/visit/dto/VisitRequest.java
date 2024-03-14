package com.hdjunction.homework.api.presentation.visit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class VisitRequest {

    private long hospitalId;
    private long patientId;
    private LocalDateTime receptionDateTime;

}
