package com.caubogeo.bogeo.dto.medicine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomMedicineMakeResponseDto {
    private boolean isSuccess;
    private String medicineImageUrl;
}
