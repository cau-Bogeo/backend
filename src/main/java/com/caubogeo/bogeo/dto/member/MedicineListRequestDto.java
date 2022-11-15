package com.caubogeo.bogeo.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MedicineListRequestDto {
    private int year;
    private int month;
    private int day;
}
