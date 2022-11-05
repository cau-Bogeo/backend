package com.caubogeo.bogeo.dto.medicine;

import com.caubogeo.bogeo.domain.medicine.MedicineDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicineDetailDto {
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

    public static MedicineDetailDto of(MedicineDetail medicineDetail) {
        return MedicineDetailDto.builder()
                .itemSeq(medicineDetail.getItemSeq())
                .itemName(medicineDetail.getItemName())
                .entpName(medicineDetail.getEntpName())
                .medicineCode(medicineDetail.getMedicineCode())
                .medicineEffect(medicineDetail.getMedicineEffect())
                .medicineDosage(medicineDetail.getMedicineDosage())
                .medicineWarning(medicineDetail.getMedicineWarning())
                .storageMethod(medicineDetail.getStorageMethod())
                .mainItemIngredient(medicineDetail.getMainItemIngredient().toArray(new String[0]))
                .validTerm(medicineDetail.getValidTerm())
                .image(medicineDetail.getImage())
                .className(medicineDetail.getClassName())
                .build();
    }
}
