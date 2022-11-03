package com.caubogeo.bogeo.service.medicine;

import com.caubogeo.bogeo.dto.medicine.MedicineResponseDto;
import com.caubogeo.bogeo.repository.MedicineDetailRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicineSearchService {
    private final MedicineDetailRepository medicineDetailRepository;

    public List<MedicineResponseDto> searchMedicineName(String name) {
        List<MedicineResponseDto> medicineDetailList = new ArrayList<>();
        medicineDetailRepository.findByItemNameContains(name).forEach(medicineQuery -> medicineDetailList.add(MedicineResponseDto.of(medicineQuery)));
        return medicineDetailList;
    }
}
