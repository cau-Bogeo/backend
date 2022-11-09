package com.caubogeo.bogeo.controller.user;

import com.caubogeo.bogeo.dto.profile.ProfileResponseDto;
import com.caubogeo.bogeo.service.profile.ProfileService;
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

import java.nio.charset.StandardCharsets;

@Slf4j
@RequestMapping("/profile")
@RequiredArgsConstructor
@RestController
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileResponseDto> getMemberProfile(@RequestParam("id") String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        log.debug("profileResponseDto = {}", profileService.getMemberProfile(id));
        return new ResponseEntity<>(profileService.getMemberProfile(id), headers, HttpStatus.OK);
    }
}
