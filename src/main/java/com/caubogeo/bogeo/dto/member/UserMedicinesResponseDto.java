package com.caubogeo.bogeo.dto.member;

import com.caubogeo.bogeo.domain.member.Medicine;
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
    private String medicineSeq;
    private String medicineName;
    private PeriodType periodType;
    private String period;
    private boolean hasEndDay;
    private LocalDate endDay;
    private boolean hasMedicineTime;
    private LocalTime medicineTime;
    private int dosage;
    private boolean isActivated;

    public UserMedicinesResponseDto(Medicine medicine, String medicineName) {
        this.medicineSeq = medicine.getMedicineSeq();
        this.medicineName = medicineName;
        this.periodType = medicine.getPeriodType();
        this.period = medicine.getPeriod();
        this.hasEndDay = medicine.isHasEndDay();
        this.endDay = medicine.getEndDay();
        this.hasMedicineTime = medicine.isHasMedicineTime();
        this.medicineTime = medicine.getMedicineTime();
        this.dosage = medicine.getDosage();
        this.isActivated = medicine.isActivated();
    }
}
