package com.caubogeo.bogeo.service.member;

import com.caubogeo.bogeo.domain.medicine.CustomMedicine;
import com.caubogeo.bogeo.domain.medicine.MedicineDetail;
import com.caubogeo.bogeo.domain.medicine.MedicineSchedule;
import com.caubogeo.bogeo.domain.member.Medicine;
import com.caubogeo.bogeo.domain.member.Member;
import com.caubogeo.bogeo.domain.member.PeriodType;
import com.caubogeo.bogeo.dto.member.MyPageResponseDto;
import com.caubogeo.bogeo.dto.member.UserMedicineRequestDto;
import com.caubogeo.bogeo.dto.member.UserMedicinesResponseDto;
import com.caubogeo.bogeo.exceptionhandler.MedicineException;
import com.caubogeo.bogeo.exceptionhandler.MedicineExceptionType;
import com.caubogeo.bogeo.exceptionhandler.MemberException;
import com.caubogeo.bogeo.exceptionhandler.MemberExceptionType;
import com.caubogeo.bogeo.repository.CustomMedicineRepository;
import com.caubogeo.bogeo.repository.MedicineDetailRepository;
import com.caubogeo.bogeo.repository.MedicineScheduleRepository;
import com.caubogeo.bogeo.repository.MemberRepository;
import com.caubogeo.bogeo.repository.UserMedicineRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserMedicineService {
    private final MemberRepository memberRepository;
    private final UserMedicineRepository medicineRepository;
    private final MedicineDetailRepository medicineDetailRepository;
    private final CustomMedicineRepository customMedicineRepository;
    private final MedicineScheduleRepository medicineScheduleRepository;

    @Transactional
    public Medicine makeUserMedicine(String id, UserMedicineRequestDto requestDto) {
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
        return medicine;
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
            if(isValidDay(medicine, date)) {  // 유효한 날짜일 때
                if(medicine.getMedicineSeq() == null) {
                    CustomMedicine customMedicine = customMedicineRepository.findById(medicine.getCustomMedicineId())
                            .orElseThrow(() -> new MedicineException(MedicineExceptionType.NOT_FOUND_MEDICINE));
                    responseDtoList.add(new UserMedicinesResponseDto(medicine, customMedicine.getMedicineName(),
                            customMedicine.getMedicineImageUrl(), true));
                }
                else {
                    MedicineDetail medicineDetail = medicineDetailRepository.findByItemSeq(medicine.getMedicineSeq());
                    responseDtoList.add(new UserMedicinesResponseDto(medicine, medicineDetail.getItemName(),
                            medicineDetail.getImage(), false));
                }
            }
        }
        return responseDtoList;
    }

    public boolean isValidDay(Medicine medicine, LocalDate givenDate) {
        LocalDate createdDate = medicine.getCreatedDate().toLocalDate();
        if(createdDate.isAfter(givenDate)) {
            return false;
        }
        if(medicine.isHasEndDay() && givenDate.isAfter(medicine.getEndDay())) {
            return false;
        }
        if(medicine.getPeriodType() == PeriodType.EVERY_DAY) {
            return true;
        }
        else if(medicine.getPeriodType() == PeriodType.SPECIFIC_PERIOD) {
            long betweenDays = ChronoUnit.DAYS.between(createdDate, givenDate);
            long period = Long.parseLong(medicine.getPeriod());
            if(betweenDays % period == 0) {
                return true;
            }
            return false;
        }
        else {
            List<Integer> weekDays = Arrays.stream(medicine.getPeriod().split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            for(Integer weekDay : weekDays) {
                int day = givenDate.getDayOfWeek().getValue();
                if(weekDay.equals(day)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Transactional
    public void changeMedicineActivation(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new MedicineException(MedicineExceptionType.NOT_FOUND_MEDICINE));
        medicine.setActivated(!medicine.isActivated());
        medicineRepository.save(medicine);
    }

    @Transactional
    public void deleteMedicine(Long id) {
        medicineRepository.deleteById(id);
    }

    public List<MyPageResponseDto> getMyMedicines(String id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_USER));
        List<Medicine> myMedicines = medicineRepository.findByUser(member);
        List<MyPageResponseDto> responseDtos = new ArrayList<>();
        for(Medicine medicine : myMedicines) {
            MedicineDetail medicineDetail = medicineDetailRepository.findByItemSeq(medicine.getMedicineSeq());
            responseDtos.add(new MyPageResponseDto(medicine, medicineDetail.getItemName(), medicineDetail.getImage()));
        }
        return responseDtos;
    }

    @Transactional
    @Async("scheduleAsyncExecutor")
    public void makeMedicineSchedule(Medicine medicine){
        List<MedicineSchedule> medicineSchedules = new ArrayList<>();
        // 종료일 존재하는 경우
        // 종료일까지 데이터 삽입
        LocalDate medicineDate = medicine.getCreatedDate().toLocalDate();
        LocalDate endDay;
        Long medicineUser = medicine.getUser().getMemberId();

        if(medicine.isHasEndDay()) {
            endDay = medicine.getEndDay();
        }
        else {
            endDay = medicineDate.plusDays(100);
        }

        if(startDayIsTakingDay(medicine)) {
            makeMedicineSchedule(medicineSchedules, medicineUser, medicineDate, medicine);
        }
        long daysToAdd = 0;
        if(medicine.getPeriodType() == PeriodType.EVERY_DAY) daysToAdd = 1;
        else if(medicine.getPeriodType() == PeriodType.SPECIFIC_PERIOD) daysToAdd = Integer.parseInt(medicine.getPeriod());

        while(medicineDate.isBefore(endDay)) {
            if(medicine.getPeriodType() == PeriodType.WEEK_DAY) {  // 주기가 요일일 때
                List<DayOfWeek> weekDays = Arrays.stream(medicine.getPeriod().split(","))
                        .map(day -> DayOfWeek.of(Integer.parseInt(day)))
                        .collect(Collectors.toList());
                for(DayOfWeek weekDay : weekDays) {
                    LocalDate nextDate = medicineDate.with(TemporalAdjusters.next(weekDay));
                    if(nextDate.isBefore(endDay) || nextDate.isEqual(endDay)) {
                        makeMedicineSchedule(medicineSchedules, medicineUser, nextDate, medicine);
                    }
                    medicineDate = nextDate;
                }
                continue;
            }
            LocalDate nextDate = medicineDate.plusDays(daysToAdd);
            if(nextDate.isAfter(endDay)) {
                break;
            }
            makeMedicineSchedule(medicineSchedules, medicineUser, nextDate, medicine);
            medicineDate = nextDate;
        }

        medicineScheduleRepository.saveAll(medicineSchedules);
    }

    public boolean startDayIsTakingDay(Medicine medicine) {
        if(medicine.getPeriodType() != PeriodType.WEEK_DAY) {   // 요일 기준 아닌 경우
            return true;
        }
        // 요일 기준
        LocalDate startDate = medicine.getCreatedDate().toLocalDate();
        List<DayOfWeek> weekDays = Arrays.stream(medicine.getPeriod().split(","))
                .map(day -> DayOfWeek.of(Integer.parseInt(day)))
                .collect(Collectors.toList());
        for(DayOfWeek weekDay: weekDays) {
            if(startDate.getDayOfWeek() == weekDay) {
                return true;
            }
        }
        return false;
    }

    public void makeMedicineSchedule(List<MedicineSchedule> medicineSchedules, Long medicineUser, LocalDate nextDate,
                                     Medicine medicine) {
        medicineSchedules.add(MedicineSchedule.builder()
                .medicineId(medicine.getId())
                .scheduleDate(nextDate)
                .isActivated(false)
                .userId(medicineUser)
                .build()
        );
    }
}
