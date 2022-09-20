package com.caubogeo.bogeo.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomException(JwtException e) {
        return ErrorResponseEntity.toResponseEntity(e.getExceptionType());
    }

    @ExceptionHandler(MemberException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomException(MemberException e) {
        return ErrorResponseEntity.toResponseEntity(e.getExceptionType());
    }

    @ExceptionHandler(AuthException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomException(AuthException e) {
        return ErrorResponseEntity.toResponseEntity(e.getExceptionType());
    }
}
