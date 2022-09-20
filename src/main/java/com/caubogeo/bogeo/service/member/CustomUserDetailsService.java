package com.caubogeo.bogeo.service.member;

import com.caubogeo.bogeo.domain.auth.Authority;
import com.caubogeo.bogeo.domain.member.Member;
import com.caubogeo.bogeo.exceptionhandler.JwtException;
import com.caubogeo.bogeo.exceptionhandler.MemberException;
import com.caubogeo.bogeo.exceptionhandler.MemberExceptionType;
import com.caubogeo.bogeo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService  implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.debug("CustomUserDetailsService -> id : {}", userId);
        return memberRepository.findById(userId)
                .map(this::createUserDetails)
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_USER));
    }

    @Transactional(readOnly = true)
    public Member getMember(String userId) throws JwtException {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_USER));
    }

    private UserDetails createUserDetails(Member member) {
        List<SimpleGrantedAuthority> authorityList = member.getAuthorities()
                .stream()
                .map(Authority::getAuthorityName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        authorityList.forEach(o -> log.debug("authList -> {}", o.getAuthority()));

        return new User(
                member.getId(),
                member.getPassword(),
                authorityList
        );
    }

}
