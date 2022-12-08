package com.caubogeo.bogeo.dto.member;

import com.caubogeo.bogeo.domain.medicine.MedicineSchedule;
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
    private Long medicineId;
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
    private String medicineImage;
    private Long customMedicineId;
    private Long medicineScheduleId;

    public UserMedicinesResponseDto(Medicine medicine, MedicineSchedule medicineSchedule,
                                    String medicineName, String medicineImage) {
        this.medicineId = medicine.getId();
        this.medicineSeq = medicine.getMedicineSeq();
        this.medicineName = medicineName;
        this.periodType = medicine.getPeriodType();
        this.period = medicine.getPeriod();
        this.hasEndDay = medicine.isHasEndDay();
        this.endDay = medicine.getEndDay();
        this.hasMedicineTime = medicine.isHasMedicineTime();
        this.medicineTime = medicine.getMedicineTime();
        this.dosage = medicine.getDosage();
        this.isActivated = medicineSchedule.isActivated();
        this.medicineImage = medicineImage;
        if(this.medicineSeq == null) {
            this.customMedicineId = medicine.getCustomMedicineId();
        }
        else {
            this.customMedicineId = null;
        }
        this.medicineScheduleId = medicineSchedule.getId();
    }
}
