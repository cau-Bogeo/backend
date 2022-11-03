package com.caubogeo.bogeo.dto.medicine;

import com.caubogeo.bogeo.domain.medicine.MedicineDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MedicineDetailResponseDto {
    private String itemSeq;
    private String itemName;
    private String entpName;
    private String medicineCode;
    private String medicineEffect;
    private String medicineDosage;
    private String medicineWarning;
    private String storageMethod;
    private String[] mainItemIngredient;
    private String validTerm;
    private String image;
    private String className;

    public static MedicineDetailResponseDto of(MedicineDetail medicineDetail) {
        return new MedicineDetailResponseDto(
                medicineDetail.getItemSeq(),
                medicineDetail.getItemName(),
                medicineDetail.getEntpName(),
                medicineDetail.getMedicineCode(),
                medicineDetail.getMedicineEffect(),
                medicineDetail.getMedicineDosage(),
                medicineDetail.getMedicineWarning(),
                medicineDetail.getStorageMethod(),
                medicineDetail.getMainItemIngredient().toArray(new String[0]),
                medicineDetail.getValidTerm(),
                medicineDetail.getImage(),
                medicineDetail.getClassName()
        );
    }
}
