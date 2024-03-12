package com.hdjunction.homework.core.db.domain.hospital;

import com.hdjunction.homework.core.db.audit.AuditingDomain;
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
public class Hospital extends AuditingDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45, nullable = false)
    private String name;

    @Column(length = 25, nullable = false)
    private String healthcareFacilityNumber;

    @Column(length = 10, nullable = false)
    private String hospitalDirectorName;

}
