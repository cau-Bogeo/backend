package com.caubogeo.bogeo.service.medicine;

import com.caubogeo.bogeo.domain.medicine.MedicineDetail;
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
        // TODO: 레포지토리에서 약 가져오기
        List<MedicineDetail> medicineDetailList = medicineDetailRepository.findByItemNameContains(name);
        List<MedicineResponseDto> responseDtoList = new ArrayList<>();
        for(MedicineDetail medicineDetail: medicineDetailList) {
            responseDtoList.add(MedicineResponseDto.of(medicineDetail));
        }
        return responseDtoList;
    }
}
