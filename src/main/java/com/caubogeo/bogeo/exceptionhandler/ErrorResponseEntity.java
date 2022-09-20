package com.caubogeo.bogeo.exceptionhandler;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ErrorResponseEntity {
    private int status;
    private String code;
    private String message;

    public static ResponseEntity<ErrorResponseEntity> toResponseEntity(BaseExceptionType type) {
        return ResponseEntity
                .status(type.getHttpStatus())
                .body(ErrorResponseEntity.builder()
                        .status(type.getHttpStatus().value())
                        .code(type.getErrorCode())
                        .message(type.getMessage())
                        .build());
    }
}
