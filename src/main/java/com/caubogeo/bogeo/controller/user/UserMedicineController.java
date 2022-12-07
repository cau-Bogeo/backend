package com.caubogeo.bogeo.controller.user;

import com.caubogeo.bogeo.domain.member.Medicine;
import com.caubogeo.bogeo.dto.member.MyPageResponseDto;
import com.caubogeo.bogeo.dto.member.UserMedicineRequestDto;
import com.caubogeo.bogeo.dto.member.UserMedicinesResponseDto;
import com.caubogeo.bogeo.service.member.UserMedicineService;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/medicine")
@RequiredArgsConstructor
@RestController
public class UserMedicineController {
    private final UserMedicineService userMedicineService;

    @PostMapping
    public ResponseEntity<Void> makeUserMedicine(@RequestParam("id") String id, @RequestBody UserMedicineRequestDto requestDto) {
        Medicine medicine = userMedicineService.makeUserMedicine(id, requestDto);
        userMedicineService.makeMedicineSchedule(medicine);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserMedicinesResponseDto>> getUserMedicines(@RequestParam("id") String id, @RequestParam("year") int year,
                                                                           @RequestParam("month") int month, @RequestParam("day") int day) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(userMedicineService.getUserMedicines(id, year, month, day), headers, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<Void> changeMedicineActivation(@RequestParam("id") Long id) {
        userMedicineService.changeMedicineActivation(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMedicine(@RequestParam("id") Long id) {
        userMedicineService.deleteMedicine(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @GetMapping("/mypage")
    public ResponseEntity<List<MyPageResponseDto>> getMyMedicine(@RequestParam("id") String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(userMedicineService.getMyMedicines(id), headers, HttpStatus.OK);
    }
}
