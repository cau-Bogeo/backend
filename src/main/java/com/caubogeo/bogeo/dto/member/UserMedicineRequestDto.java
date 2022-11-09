package com.caubogeo.bogeo.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMedicineRequestDto {
    private String medicineSeq;
    private int periodType;
    private String period;
    private boolean hasEndDay;
    private String endDay;
    private boolean hasMedicineTime;
    private int medicineHour;
    private int medicineMinute;
    private int dosage;
}
