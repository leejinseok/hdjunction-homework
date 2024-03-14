package com.hdjunction.homework.api.presentation.patient.dto;

import com.hdjunction.homework.core.db.domain.common.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class PatientRequest {

    private long hospitalId;
    private String name;
    private String genderCode;
    private String birth;
    private PhoneNumber phoneNumber;

}
