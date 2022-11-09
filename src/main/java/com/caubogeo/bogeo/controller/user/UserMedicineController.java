package com.caubogeo.bogeo.controller.user;

import com.caubogeo.bogeo.dto.member.UserMedicineRequestDto;
import com.caubogeo.bogeo.service.member.UserMedicineService;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
        userMedicineService.makeUserMedicine(id, requestDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}
