package com.caubogeo.bogeo.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MedicineExceptionType implements BaseExceptionType {
    OUT_OF_DATE_END_DATE("OUT_OF_DATE_END_DATE", "끝나는 날짜는 과거의 날짜가 될 수 없습니다.", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
}
