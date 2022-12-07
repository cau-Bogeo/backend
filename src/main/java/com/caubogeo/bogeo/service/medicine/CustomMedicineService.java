package com.caubogeo.bogeo.service.medicine;

import com.caubogeo.bogeo.domain.medicine.CustomMedicine;
import com.caubogeo.bogeo.domain.member.Medicine;
import com.caubogeo.bogeo.domain.member.Member;
import com.caubogeo.bogeo.domain.member.PeriodType;
import com.caubogeo.bogeo.dto.medicine.CustomMedicineMakeResponseDto;
import com.caubogeo.bogeo.dto.medicine.CustomMedicineRequestDto;
import com.caubogeo.bogeo.exceptionhandler.MedicineException;
import com.caubogeo.bogeo.exceptionhandler.MedicineExceptionType;
import com.caubogeo.bogeo.exceptionhandler.MemberException;
import com.caubogeo.bogeo.exceptionhandler.MemberExceptionType;
import com.caubogeo.bogeo.repository.CustomMedicineRepository;
import com.caubogeo.bogeo.repository.MemberRepository;
import com.caubogeo.bogeo.repository.UserMedicineRepository;
import com.caubogeo.bogeo.service.S3.S3Uploader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomMedicineService {
    private final CustomMedicineRepository customMedicineRepository;
    private final UserMedicineRepository userMedicineRepository;
    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public CustomMedicineMakeResponseDto makeCustomMedicine(MultipartFile image, CustomMedicineRequestDto requestDto) {
        String customMedicineImage;
        if (image.isEmpty()) {
            throw new MedicineException(MedicineExceptionType.EMPTY_IMAGE);
        } else {
            try {
                customMedicineImage = s3Uploader.upload(image);
            } catch (IOException e) {
                throw new MedicineException(MedicineExceptionType.INVALID_MEDICINE_IMAGE);
            }
        }

        Member user = memberRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_USER));
        CustomMedicine customMedicine = CustomMedicine.builder()
                .memberId(user.getMemberId())
                .medicineName(requestDto.getMedicineName())
                .medicineImageUrl(customMedicineImage)
                .build();
        CustomMedicine madeMedicine = customMedicineRepository.save(customMedicine);

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
                .user(user)
                .medicineSeq(null)
                .customMedicineId(madeMedicine.getId())
                .periodType(periodType)
                .period(requestDto.getPeriod())
                .hasEndDay(requestDto.isHasEndDay())
                .endDay(endDateTime)
                .hasMedicineTime(requestDto.isHasMedicineTime())
                .medicineTime(medicineTime)
                .dosage(requestDto.getDosage())
                .isActivated(false)
                .build();
        userMedicineRepository.save(medicine);

        return new CustomMedicineMakeResponseDto(true, customMedicineImage);
    }
}
