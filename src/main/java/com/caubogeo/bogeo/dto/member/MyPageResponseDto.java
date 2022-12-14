package com.caubogeo.bogeo.dto.member;

import com.caubogeo.bogeo.domain.member.Medicine;
import com.caubogeo.bogeo.domain.member.PeriodType;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytebuddy.asm.Advice.Local;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageResponseDto {
    private LocalDate createdDate;
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

    public MyPageResponseDto(Medicine medicine, String medicineName, String medicineImage) {
        this.createdDate = medicine.getCreatedDate().toLocalDate();
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
        this.isActivated = medicine.isActivated();
        this.medicineImage = medicineImage;
    }
}
