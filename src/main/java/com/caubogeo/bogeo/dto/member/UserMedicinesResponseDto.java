package com.caubogeo.bogeo.dto.member;

import com.caubogeo.bogeo.domain.member.PeriodType;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMedicinesResponseDto {
    private String medicineName;
    private PeriodType periodType;
    private String period;
    private boolean hasEndDay;
    private LocalDate endDay;
    private boolean hasMedicineTime;
    private LocalTime medicineTime;
    private int dosage;
    private boolean isActivated;
}
