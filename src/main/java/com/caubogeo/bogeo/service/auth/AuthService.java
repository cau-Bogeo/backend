package com.caubogeo.bogeo.service.auth;

import com.caubogeo.bogeo.domain.auth.Authority;
import com.caubogeo.bogeo.domain.auth.MemberAuth;
import com.caubogeo.bogeo.domain.jwt.RefreshToken;
import com.caubogeo.bogeo.domain.member.Member;
import com.caubogeo.bogeo.dto.jwt.TokenDto;
import com.caubogeo.bogeo.dto.login.LoginRequestDto;
import com.caubogeo.bogeo.dto.member.MemberRequestDto;
import com.caubogeo.bogeo.dto.member.MemberResponseDto;
import com.caubogeo.bogeo.exceptionhandler.AuthException;
import com.caubogeo.bogeo.exceptionhandler.AuthorityExceptionType;
import com.caubogeo.bogeo.exceptionhandler.MemberException;
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

    public boolean checkId(String id) {
        log.info("checkID : {}", id);
        log.info("existsByID : {}", memberRepository.existsById(id));
        return memberRepository.existsById(id);
    }

    @Transactional
    public MemberResponseDto signUp(MemberRequestDto dto) {
        if(memberRepository.existsById(dto.getId())) {
            throw new MemberException(MemberExceptionType.DUPLICATE_USER);
        }

        Authority authority = authorityRepository
                .findByAuthorityName(MemberAuth.ROLE_USER).orElseThrow(() -> new AuthException(AuthorityExceptionType.NOT_FOUND_AUTHORITY));
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

        refreshTokenRepository.findByKey(id)
                        .ifPresentOrElse(
                                (tokenEntity) -> tokenEntity.updateValue(refreshToken),
                                () -> refreshTokenRepository.save(
                                        RefreshToken.builder()
                                        .key(id)
                                        .value(refreshToken)
                                        .build())
                        );
        return tokenProvider.createTokenDto(accessToken, refreshToken);
    }

    @Transactional
    public TokenDto reissue(String refreshToken) {
        Authentication authentication = tokenProvider.getAuthentication(refreshToken);
        RefreshToken findRefreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new MemberException(MemberExceptionType.LOGOUT_MEMBER));

        if(findRefreshToken.getValue().equals(refreshToken)) {
            String id = tokenProvider.getMemberIdByToken(refreshToken);
            Member member = customUserDetailsService.getMember(id);

            String newRefreshToken = tokenProvider.createRefreshToken(id, member.getAuthorities());
            String newAccessToken = tokenProvider.createAccessToken(id, member.getAuthorities());
            findRefreshToken.updateValue(newRefreshToken);

            TokenDto tokenDto = tokenProvider.createTokenDto(newAccessToken, newRefreshToken);
            log.debug("refresh New = {} ",newRefreshToken);
            return tokenDto;
        } else {
            log.info("refresh 토큰이 일치하지 않습니다.");
            return null;
        }

//        // access token은 JWT filter에서 검증되고 온다
//        String originAccessToken = tokenRequestDto.getAccessToken();
//        String originRefreshToken = tokenRequestDto.getRefreshToken();
//
//        // refresh token 검증
//        boolean isValid = tokenProvider.validateToken(originRefreshToken);
//        log.debug("isValid = {}", isValid);
//
//        if(isValid == -1) {
//            throw new JwtException(JwtExceptionType.BAD_TOKEN);
//        }
//        else if(isValid == 2) {
//            throw new JwtException(JwtExceptionType.REFRESH_TOKEN_EXPIRED);
//        }
//
//        Authentication authentication = tokenProvider.getAuthentication(originAccessToken);
//        log.debug("Authentication = {}", authentication);
//
//        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
//                .orElseThrow(() -> new MemberException(MemberExceptionType.LOGOUT_MEMBER));
//
//        if(!refreshToken.getValue().equals(originRefreshToken)) {
//            throw new JwtException(JwtExceptionType.BAD_TOKEN); // 토큰이 일치하지 않습니다.
//        }
//
//        String id = tokenProvider.getMemberIdByToken(originAccessToken);
//        Member member = customUserDetailsService.getMember(id);
//
//        String newAccessToken = tokenProvider.createAccessToken(id, member.getAuthorities());
//        String newRefreshToken = tokenProvider.createRefreshToken(id, member.getAuthorities());
//
//        TokenDto tokenDto = tokenProvider.createTokenDto(newAccessToken, newRefreshToken);
//        log.debug("refresh Origin = {}",originRefreshToken);
//        log.debug("refresh New = {} ",newRefreshToken);
//
//        // 저장소 정보 업데이트 (dirtyChecking으로 업데이트)
//        refreshToken.updateValue(newRefreshToken);
//
//        return tokenDto;
    }
//    @Transactional
//    public String issueRefreshToken(Authentication authentication){
//
//        String newRefreshToken = tokenProvider.createRefreshToken(authentication.getName(), authentication.getAuthorities());
//
//        // 기존것이 있다면 바꿔주고, 없다면 만들어줌
//        refreshTokenRepository.findByKey(authentication.getName())
//                .ifPresentOrElse(
//                        r-> {r.updateValue(newRefreshToken);
//                            log.info("issueRefreshToken method | change token ");
//                        },
//                        () -> {
//                            RefreshToken token = RefreshToken.builder()
//                                    .key(authentication.getName())
//                                    .value(newRefreshToken)
//                                    .build();
//                            log.info(" issueRefreshToken method | save tokenID : {}, token : {}", token.getUserId(), token.getToken());
//                            refreshTokenRepository.save(token);
//                        });
//        return newRefreshToken;
//    }
}
