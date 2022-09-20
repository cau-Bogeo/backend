package com.caubogeo.bogeo.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberException extends RuntimeException{
    MemberExceptionType exceptionType;
}
