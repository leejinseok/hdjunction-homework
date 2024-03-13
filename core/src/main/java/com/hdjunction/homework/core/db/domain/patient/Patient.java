package com.hdjunction.homework.core.db.domain.patient;

import com.hdjunction.homework.core.db.audit.AuditingDomain;
import com.hdjunction.homework.core.db.domain.common.PhoneNumber;
import com.hdjunction.homework.core.db.domain.hospital.Hospital;
import com.hdjunction.homework.core.db.domain.visit.PatientVisit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uidx_patient_1", columnNames = {"hospital_id", "registration_number"})
        }
)
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    private List<PatientVisit> visits = new ArrayList<>();

    public void update(
            final Hospital hospital,
            final String name,
            final String genderCode,
            final String birth,
            final PhoneNumber phoneNumber
    ) {
        this.hospital = hospital;
        this.name = name;
        this.genderCode = genderCode;
        this.birth = birth;
        this.phoneNumber = phoneNumber;
    }

}
