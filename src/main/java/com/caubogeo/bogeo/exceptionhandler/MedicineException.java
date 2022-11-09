package com.caubogeo.bogeo.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MedicineException extends RuntimeException{
    MedicineExceptionType medicineExceptionType;
}
