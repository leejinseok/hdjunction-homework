package com.hdjunction.homework.core.db.domain.hospital;

import com.hdjunction.homework.core.db.audit.AuditingDomain;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Hospital extends AuditingDomain {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



}
