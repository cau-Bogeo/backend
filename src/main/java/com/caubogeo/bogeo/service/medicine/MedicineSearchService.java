package com.caubogeo.bogeo.service.medicine;

import com.caubogeo.bogeo.domain.medicine.AvoidCombination;
import com.caubogeo.bogeo.domain.medicine.MedicineDetail;
import com.caubogeo.bogeo.domain.member.Medicine;
import com.caubogeo.bogeo.domain.member.Member;
import com.caubogeo.bogeo.dto.medicine.MedicineCombinationDto;
import com.caubogeo.bogeo.dto.medicine.MedicineDetailResponseDto;
import com.caubogeo.bogeo.dto.medicine.MedicineResponseDto;
import com.caubogeo.bogeo.exceptionhandler.MemberException;
import com.caubogeo.bogeo.exceptionhandler.MemberExceptionType;
import com.caubogeo.bogeo.repository.CombinationRepository;
import com.caubogeo.bogeo.repository.MedicineDetailRepository;
import com.caubogeo.bogeo.repository.MemberRepository;
import com.caubogeo.bogeo.repository.UserMedicineRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicineSearchService {
    private final MedicineDetailRepository medicineDetailRepository;
    private final CombinationRepository combinationRepository;
    private final UserMedicineRepository userMedicineRepository;
    private final MemberRepository memberRepository;

    public List<MedicineResponseDto> searchMedicineName(String name) {
        List<MedicineResponseDto> medicineDetailList = new ArrayList<>();
        medicineDetailRepository.findByItemNameContains(name).forEach(medicineQuery -> medicineDetailList.add(MedicineResponseDto.of(medicineQuery)));
        return medicineDetailList;
    }

    public MedicineDetailResponseDto searchMedicineDetail(String id, String medicineSeq) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_USER));
        List<Medicine> userMedicineList = userMedicineRepository.findByUser(member);
        userMedicineList = userMedicineList.stream()
                .filter(medicine -> medicine.getMedicineSeq() != null)
                .collect(Collectors.toList());
        List<Medicine> uniqueMedicineList = DuplicationUtils.deduplication(userMedicineList, Medicine::getMedicineSeq);
        MedicineDetail medicineDetail = medicineDetailRepository.findByItemSeq(medicineSeq);
        List<MedicineCombinationDto> avoidCombinations = new ArrayList<>();

        if(uniqueMedicineList.isEmpty()) {
            return MedicineDetailResponseDto.of(medicineDetail, avoidCombinations);
        }

        for(Medicine secondMedicineSeq: uniqueMedicineList) {
            AvoidCombination combination = combinationRepository.findByFirstMedicineSeqAndSecondMedicineSeq(medicineSeq,
                    secondMedicineSeq.getMedicineSeq());
            if(combination != null) {
                MedicineCombinationDto combinationDto = new MedicineCombinationDto(combination.getSecondMedicineName(),
                        combination.getProhibitedContent());
                avoidCombinations.add(combinationDto);
            }
        }
        return MedicineDetailResponseDto.of(medicineDetail, avoidCombinations);
    }
}
