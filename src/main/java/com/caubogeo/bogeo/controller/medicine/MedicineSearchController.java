package com.caubogeo.bogeo.controller.medicine;

import com.caubogeo.bogeo.dto.medicine.MedicineDetailResponseDto;
import com.caubogeo.bogeo.dto.medicine.MedicineResponseDto;
import com.caubogeo.bogeo.service.medicine.MedicineSearchService;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/search")
@RequiredArgsConstructor
@RestController
public class MedicineSearchController {
    private final MedicineSearchService medicineSearchService;

    @GetMapping
    public ResponseEntity<List<MedicineResponseDto>> searchMedicine(@RequestParam("name") String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(medicineSearchService.searchMedicineName(name), headers, HttpStatus.OK);
    }

    @GetMapping("/detail")
    public ResponseEntity<MedicineDetailResponseDto> searchMedicineDetail(@RequestParam("id") String id, @RequestParam("seq") String medicineSeq) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(medicineSearchService.searchMedicineDetail(id, medicineSeq), headers, HttpStatus.OK);
    }
}
