package com.caubogeo.bogeo.publicdata;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
public class DataApiController {
    private final DataApiService dataApiService;

    @GetMapping("/shape")
    public void makeShapeDatabase() {
        dataApiService.setPillShapeDatabase();
    }

    @GetMapping("/combination")
    public void makeCombinationDatabase() {
        dataApiService.setCombinationDatabase();
    }

    @GetMapping("/detail")
    public void makeMedicineDetailDatabase() {
        dataApiService.setMedicineDetailDatabase();
    }
}
