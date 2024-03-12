package com.hdjunction.homework.api.presentation.patient.dto;

import com.hdjunction.homework.core.db.domain.common.PhoneNumber;
import lombok.Getter;

@Getter
public class PatientRequest {

    private long hospitalId;
    private String name;
    private String registrationNumber;
    private String genderCode;
    private String birth;
    private PhoneNumber phoneNumber;

}
