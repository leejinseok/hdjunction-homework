package com.hdjunction.homework.api.presentation.visit.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class VisitRequest {

    private long hospitalId;
    private long patientId;
    private LocalDateTime receptionDateTime;

}
