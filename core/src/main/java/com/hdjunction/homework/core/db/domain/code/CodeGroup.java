package com.hdjunction.homework.core.db.domain.code;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CodeGroup {

    @Id
    private String codeGroup;

    @Column(length = 10, nullable = false)
    private String name;

    @Column(length = 30, nullable = false)
    private String description;

}
