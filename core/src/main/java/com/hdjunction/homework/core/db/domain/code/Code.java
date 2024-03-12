package com.hdjunction.homework.core.db.domain.code;

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
public class Code {

    @Id
    private String code;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "code_group_id", foreignKey = @ForeignKey(name = "fk_code_1"))
    private CodeGroup codeGroup;

    @Column(length = 10)
    private String name;

}
