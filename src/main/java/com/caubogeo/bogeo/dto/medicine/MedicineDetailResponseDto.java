package com.caubogeo.bogeo.dto.medicine;

import com.caubogeo.bogeo.domain.medicine.MedicineDetail;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MedicineDetailResponseDto {
    private boolean hasCombination;
    private MedicineDetailDto medicineDetail;
    private List<MedicineCombinationDto> combinations;


    public static MedicineDetailResponseDto of(MedicineDetail medicineDetail, List<MedicineCombinationDto> combinations) {
        if(combinations.isEmpty()) {
            return new MedicineDetailResponseDto(false, MedicineDetailDto.of(medicineDetail), null);
        }
        return new MedicineDetailResponseDto(true, MedicineDetailDto.of(medicineDetail), combinations);
    }
}
