package com.caubogeo.bogeo.jwt;

import com.caubogeo.bogeo.exceptionhandler.BizException;
import com.caubogeo.bogeo.exceptionhandler.MemberExceptionType;
import com.caubogeo.bogeo.service.member.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomIdPasswordAuthProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    public Authentication authenticate(Authentication authentication) throws BizException {
        UserDetails user = null;
        try {
            user = retrieveUser(authentication.getName());
        } catch (BizException e) {
            throw e;
        }

        Object principalToReturn = user;
        CustomIdPasswordAuthToken result = new CustomIdPasswordAuthToken(principalToReturn, authentication.getCredentials(),
                this.authoritiesMapper.mapAuthorities(user.getAuthorities()));
        additionalAuthenticationChecks(user, result);
        result.setDetails(authentication.getDetails());
        return result;
    }

    protected void additionalAuthenticationChecks(UserDetails userDetails, CustomIdPasswordAuthToken authentication) throws BizException {
        log.debug("additionalAuthenticationChecks authentication = {}", authentication);
        if (authentication.getCredentials() == null) {
            log.debug("additionalAuthenticationChecks is null !");
            throw new BizException(MemberExceptionType.NOT_FOUND_PASSWORD);
        }

        String presentedPassword = authentication.getCredentials().toString();
        log.debug("authentication.presentedPassword = {}", presentedPassword);

        if(!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            throw new BizException(MemberExceptionType.WRONG_PASSWORD);
        }
    }

    protected final UserDetails retrieveUser(String username) throws BizException {
        try {
            UserDetails loadedUser = customUserDetailsService.loadUserByUsername(username);
            if(loadedUser == null) {
                throw new InternalAuthenticationServiceException(
                        "UserDetailsService returned null, which is an interface contract violation"
                );
            }
            return loadedUser;
        } catch (BizException e) {
            log.debug("error in retrieveUser = {}", e.getMessage());
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(
                    "내부 인증 로직 중 알 수 없는 오류가 발생했습니다."
            );
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(CustomIdPasswordAuthToken.class);
    }
}
