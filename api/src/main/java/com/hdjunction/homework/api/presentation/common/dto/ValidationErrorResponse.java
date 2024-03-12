package com.hdjunction.homework.api.presentation.common.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationErrorResponse {

    public static final String USER_ID_MUST_NOT_NULL = "아이디는 필수항목입니다";
    public static final String PASSWORD_MUST_NOT_NULL = "패스워드는 필수항목입니다";
    public static final String NAME_MUST_NOT_NULL = "이름은 필수항목입니다";
    public static final String REG_NO_IS_NOT_VALID = "잘못 된 주민등록 번호입니다.";

    private final List<String> errors = new ArrayList<>();

}
