package com.hdjunction.homework.core.db.domain.common;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PhoneNumber {

    private String number1;
    private String number2;
    private String number3;

    public String format() {
        return "%s-%s-%s".formatted(number1, number2, number3);
    }

}
