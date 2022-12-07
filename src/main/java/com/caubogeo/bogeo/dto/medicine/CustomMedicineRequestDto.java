package com.caubogeo.bogeo.dto.medicine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomMedicineRequestDto {
    private String userId;
    private String medicineName;
    private int periodType;
    private String period;
    private boolean hasEndDay;
    private String endDay;
    private boolean hasMedicineTime;
    private int medicineHour;
    private int medicineMinute;
    private int dosage;
}
