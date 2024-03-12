package com.hdjunction.homework.core.db.domain.patient;

import com.hdjunction.homework.core.db.audit.AuditingDomain;
import com.hdjunction.homework.core.db.domain.common.PhoneNumber;
import com.hdjunction.homework.core.db.domain.hospital.Hospital;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Patient extends AuditingDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false, foreignKey = @ForeignKey(name = "fk_patient_1"))
    private Hospital hospital;

    @Column(length = 45, nullable = false)
    private String name;

    @Column(length = 13, nullable = false)
    private String registrationNumber;

    @Column(length = 10, nullable = false)
    private String genderCode;

    @Column(length = 10)
    private String birth;

    @Embedded
    @Column(length = 20)
    private PhoneNumber phoneNumber;

    public void update(
            final Hospital hospital,
            final String name,
            final String registrationNumber,
            final String genderCode,
            final String birth,
            final PhoneNumber phoneNumber
    ) {
        this.hospital = hospital;
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.genderCode = genderCode;
        this.birth = birth;
        this.phoneNumber = phoneNumber;
    }

}
