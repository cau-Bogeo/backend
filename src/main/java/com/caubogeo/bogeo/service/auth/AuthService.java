package com.caubogeo.bogeo.service.auth;

import com.caubogeo.bogeo.domain.auth.Authority;
import com.caubogeo.bogeo.domain.member.Member;
import com.caubogeo.bogeo.domain.auth.MemberAuth;
import com.caubogeo.bogeo.domain.jwt.RefreshToken;
import com.caubogeo.bogeo.dto.jwt.TokenDto;
import com.caubogeo.bogeo.dto.jwt.TokenRequestDto;
import com.caubogeo.bogeo.dto.login.LoginRequestDto;
import com.caubogeo.bogeo.dto.member.MemberRequestDto;
import com.caubogeo.bogeo.dto.member.MemberResponseDto;
import com.caubogeo.bogeo.exceptionhandler.AuthorityExceptionType;
import com.caubogeo.bogeo.exceptionhandler.BizException;
import com.caubogeo.bogeo.exceptionhandler.JwtExceptionType;
import com.caubogeo.bogeo.exceptionhandler.MemberExceptionType;
import com.caubogeo.bogeo.jwt.CustomIdPasswordAuthToken;
import com.caubogeo.bogeo.jwt.TokenProvider;
import com.caubogeo.bogeo.repository.AuthorityRepository;
import com.caubogeo.bogeo.repository.MemberRepository;
import com.caubogeo.bogeo.repository.RefreshTokenRepository;
import com.caubogeo.bogeo.service.member.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomUserDetailsService customUserDetailsService;

    @Transactional
    public MemberResponseDto signUp(MemberRequestDto dto) {
        if(memberRepository.existsById(dto.getId())) {
            throw new BizException(MemberExceptionType.DUPLICATE_USER);
        }

        Authority authority = authorityRepository
                .findByAuthorityName(MemberAuth.ROLE_USER).orElseThrow(() -> new BizException(AuthorityExceptionType.NOT_FOUND_AUTHORITY));

        Set<Authority> set = new HashSet<>();
        set.add(authority);

        Member member = dto.toMember(passwordEncoder, set);
        log.debug("member = {}" , member);
        return MemberResponseDto.of(memberRepository.save(member));
    }

    @Transactional
    public TokenDto login(LoginRequestDto loginRequestDto) {
        CustomIdPasswordAuthToken customIdPasswordAuthToken = new CustomIdPasswordAuthToken(loginRequestDto.getId(), loginRequestDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(customIdPasswordAuthToken);
        String id = authentication.getName();
        Member member = customUserDetailsService.getMember(id);

        String accessToken = tokenProvider.createAccessToken(id, member.getAuthorities());
        String refreshToken = tokenProvider.createRefreshToken(id, member.getAuthorities());

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .key(id)
                        .value(refreshToken)
                        .build()
        );

        return tokenProvider.createTokenDto(accessToken, refreshToken);
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // access token은 JWT filter에서 검증되고 온다
        String originAccessToken = tokenRequestDto.getAccessToken();
        String originRefreshToken = tokenRequestDto.getRefreshToken();

        // refresh token 검증
        int refreshTokenFlag = tokenProvider.validateToken(originRefreshToken);
        log.debug("refreshTokenFlag = {}", refreshTokenFlag);

        if(refreshTokenFlag == -1) {
            throw new BizException(JwtExceptionType.BAD_TOKEN);  // 잘못된 리프레시 토큰
        } else if(refreshTokenFlag == 2) {
            throw new BizException(JwtExceptionType.REFRESH_TOKEN_EXPIRED);  // 유효기간이 끝난 토큰
        }

        Authentication authentication = tokenProvider.getAuthentication(originAccessToken);

        log.debug("Authentication = {}", authentication);

        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new BizException(MemberExceptionType.LOGOUT_MEMBER));

        if(!refreshToken.getValue().equals(originRefreshToken)) {
            throw new BizException(JwtExceptionType.BAD_TOKEN); // 토큰이 일치하지 않습니다.
        }

        String id = tokenProvider.getMemberIdByToken(originAccessToken);
        Member member = customUserDetailsService.getMember(id);

        String newAccessToken = tokenProvider.createAccessToken(id, member.getAuthorities());
        String newRefreshToken = tokenProvider.createRefreshToken(id, member.getAuthorities());

        TokenDto tokenDto = tokenProvider.createTokenDto(newAccessToken, newRefreshToken);
        log.debug("refresh Origin = {}",originRefreshToken);
        log.debug("refresh New = {} ",newRefreshToken);

        // 저장소 정보 업데이트 (dirtyChecking으로 업데이트)
        refreshToken.updateValue(newRefreshToken);

        return tokenDto;
    }
}
