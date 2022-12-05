package com.caubogeo.bogeo.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MedicineExceptionType implements BaseExceptionType {
    OUT_OF_DATE_END_DATE("OUT_OF_DATE_END_DATE", "끝나는 날짜는 과거의 날짜가 될 수 없습니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_MEDICINE("NOT_FOUND_MEDICINE", "약을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    EMPTY_IMAGE("EMPTY_IMAGE", "비어 있는 사진 파일입니다.", HttpStatus.BAD_REQUEST),
    INVALID_MEDICINE_IMAGE("INVALID_MEDICINE_IMAGE", "유효하지 않은 사진 파일입니다.", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
}
