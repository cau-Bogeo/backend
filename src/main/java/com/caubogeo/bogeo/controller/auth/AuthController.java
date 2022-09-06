package com.caubogeo.bogeo.controller.auth;

import com.caubogeo.bogeo.dto.UserRegisterRequestDto;
import com.caubogeo.bogeo.dto.jwt.TokenDto;
import com.caubogeo.bogeo.dto.jwt.TokenRequestDto;
import com.caubogeo.bogeo.dto.login.LoginRequestDto;
import com.caubogeo.bogeo.dto.member.MemberRequestDto;
import com.caubogeo.bogeo.dto.member.MemberResponseDto;
import com.caubogeo.bogeo.service.auth.AuthService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto memberRequestDto) {
        log.debug("memberRequestDto = {}", memberRequestDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(authService.signUp(memberRequestDto), headers, HttpStatus.OK);
    }

    @PostMapping("/login")
    public TokenDto login(@RequestBody LoginRequestDto loginRequestDto) {
        return authService.login(loginRequestDto);
    }

    @PostMapping("/reissue")
    public TokenDto reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return authService.reissue(tokenRequestDto);
    }
}
