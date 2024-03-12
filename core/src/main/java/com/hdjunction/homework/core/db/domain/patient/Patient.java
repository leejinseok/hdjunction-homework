package com.hdjunction.homework.core.db.domain.patient;

import com.hdjunction.homework.core.db.audit.AuditingDomain;
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
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @Column(length = 45, nullable = false)
    private String name;

    @Column(length = 13, nullable = false)
    private String registrationNumber;

    @Column(length = 10, nullable = false)
    private String genderCode;

    @Column(length = 10)
    private String birth;

    @Column(length = 20)
    private String phoneNumber;

}
