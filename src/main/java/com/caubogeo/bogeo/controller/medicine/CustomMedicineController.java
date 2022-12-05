package com.caubogeo.bogeo.controller.medicine;

import com.caubogeo.bogeo.dto.medicine.CustomMedicineMakeResponseDto;
import com.caubogeo.bogeo.dto.medicine.CustomMedicineRequestDto;
import com.caubogeo.bogeo.service.medicine.CustomMedicineService;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/custom/medicine")
@RequiredArgsConstructor
@Slf4j
public class CustomMedicineController {
    private final CustomMedicineService customMedicineService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CustomMedicineMakeResponseDto> makeCustomMedicine(@RequestPart("file") MultipartFile file,
                                                                            @RequestPart("medicineDto")CustomMedicineRequestDto medicineDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(customMedicineService.makeCustomMedicine(file, medicineDto), headers, HttpStatus.OK);
    }
}
