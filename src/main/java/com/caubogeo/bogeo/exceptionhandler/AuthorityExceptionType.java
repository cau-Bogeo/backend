package com.caubogeo.bogeo.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthorityExceptionType implements BaseExceptionType{
    NOT_FOUND_AUTHORITY("NOT_FOUND_AUTHORITY", "존재하지 않는 권한입니다.", HttpStatus.BAD_REQUEST),
    NO_AUTHORIZATION("NO_AUTHORIZATION", "인증 정보가 없습니다.", HttpStatus.UNAUTHORIZED),
    INTERNAL_AUTH_EXCEPTION("INTERNAL_AUTH_ERROR", "내부 인증 로직 중 알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
}
