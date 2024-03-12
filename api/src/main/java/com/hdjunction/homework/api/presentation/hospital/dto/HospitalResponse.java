package com.hdjunction.homework.api.presentation.hospital.dto;

import com.hdjunction.homework.core.db.domain.hospital.Hospital;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class HospitalResponse {

    private long id;
    private String name;
    private String healthcareFacilityNumber;
    private String hospitalDirectorName;

    public static HospitalResponse create(final Hospital hospital) {
        return HospitalResponse.of(
                hospital.getId(),
                hospital.getName(),
                hospital.getHealthcareFacilityNumber(),
                hospital.getHospitalDirectorName()
        );
    }

}
