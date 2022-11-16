package com.caubogeo.bogeo.service.member;

import com.caubogeo.bogeo.domain.medicine.MedicineDetail;
import com.caubogeo.bogeo.domain.member.Medicine;
import com.caubogeo.bogeo.domain.member.Member;
import com.caubogeo.bogeo.domain.member.PeriodType;
import com.caubogeo.bogeo.dto.member.UserMedicineRequestDto;
import com.caubogeo.bogeo.dto.member.UserMedicinesResponseDto;
import com.caubogeo.bogeo.exceptionhandler.MedicineException;
import com.caubogeo.bogeo.exceptionhandler.MedicineExceptionType;
import com.caubogeo.bogeo.exceptionhandler.MemberException;
import com.caubogeo.bogeo.exceptionhandler.MemberExceptionType;
import com.caubogeo.bogeo.repository.MedicineDetailRepository;
import com.caubogeo.bogeo.repository.MemberRepository;
import com.caubogeo.bogeo.repository.UserMedicineRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserMedicineService {
    private final MemberRepository memberRepository;
    private final UserMedicineRepository medicineRepository;
    private final MedicineDetailRepository medicineDetailRepository;

    @Transactional
    public void makeUserMedicine(String id, UserMedicineRequestDto requestDto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_USER));
        PeriodType periodType = PeriodType.valueOfLabel(requestDto.getPeriodType());
        LocalTime medicineTime = null;
        LocalDate endDateTime = null;
        if(requestDto.isHasMedicineTime()) {
            medicineTime = LocalTime.of(requestDto.getMedicineHour(), requestDto.getMedicineMinute());
        }
        if(requestDto.isHasEndDay()) {
            String[] endDayRaw = requestDto.getEndDay().split("-");
            int[] endDay = Arrays.stream(endDayRaw).mapToInt(Integer::parseInt).toArray();
            log.info("endDay: {} {} {}", endDay[0], endDay[1], endDay[2]);
            endDateTime = LocalDate.of(endDay[0], endDay[1], endDay[2]);
        }

        if(endDateTime != null && endDateTime.isBefore(LocalDate.now())) {
            throw new MedicineException(MedicineExceptionType.OUT_OF_DATE_END_DATE);
        }

        Medicine medicine = Medicine.builder()
                .user(member)
                .medicineSeq(requestDto.getMedicineSeq())
                .periodType(periodType)
                .period(requestDto.getPeriod())
                .hasEndDay(requestDto.isHasEndDay())
                .endDay(endDateTime)
                .hasMedicineTime(requestDto.isHasMedicineTime())
                .medicineTime(medicineTime)
                .dosage(requestDto.getDosage())
                .isActivated(false)
                .build();

        medicineRepository.save(medicine);
    }

    public List<UserMedicinesResponseDto> getUserMedicines(String id, int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_USER));
        List<Medicine> medicineList = medicineRepository.findByUser(member);
        List<UserMedicinesResponseDto> responseDtoList = new ArrayList<>();
        for(Medicine medicine : medicineList) {
            if(medicine.isHasEndDay() && medicine.getEndDay().isBefore(date)) {
                continue;
            }
            MedicineDetail medicineDetail = medicineDetailRepository.findByItemSeq(medicine.getMedicineSeq());
            responseDtoList.add(new UserMedicinesResponseDto(medicine, medicineDetail.getItemName(), medicineDetail.getImage()));
        }
        return responseDtoList;
    }

    @Transactional
    public void changeMedicineActivation(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new MedicineException(MedicineExceptionType.NOT_FOUND_MEDICINE));
        medicine.setActivated(!medicine.isActivated());
        medicineRepository.save(medicine);
    }
}
