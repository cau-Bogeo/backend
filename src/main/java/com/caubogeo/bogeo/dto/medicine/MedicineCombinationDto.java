package com.caubogeo.bogeo.dto.medicine;

import com.caubogeo.bogeo.domain.medicine.AvoidCombination;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MedicineCombinationDto {
    private String medicineName;
    private String prohibitContent;

    public static MedicineCombinationDto of(AvoidCombination combination) {
        return new MedicineCombinationDto(combination.getSecondMedicineName(), combination.getProhibitedContent());
    }
}
