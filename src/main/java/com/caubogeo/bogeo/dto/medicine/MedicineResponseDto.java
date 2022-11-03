package com.caubogeo.bogeo.dto.medicine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MedicineResponseDto {
    private String itemSeq;
    private String itemName;
    private String medicineCode;
    private String image;
    private String className;

    public static MedicineResponseDto of(MedicineQuery medicineQuery) {
        return new MedicineResponseDto(
                medicineQuery.getItemSeq(),
                medicineQuery.getItemName(),
                medicineQuery.getMedicineCode(),
                medicineQuery.getImage(),
                medicineQuery.getClassName()
        );
    }
}
