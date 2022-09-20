package com.caubogeo.bogeo.jwt;

import com.caubogeo.bogeo.exceptionhandler.*;
import com.caubogeo.bogeo.service.member.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
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
    public Authentication authenticate(Authentication authentication) throws JwtException {
        UserDetails user = null;
        try {
            user = retrieveUser(authentication.getName());
        } catch (JwtException e) {
            throw e;
        }

        Object principalToReturn = user;
        CustomIdPasswordAuthToken result = new CustomIdPasswordAuthToken(principalToReturn, authentication.getCredentials(),
                this.authoritiesMapper.mapAuthorities(user.getAuthorities()));
        additionalAuthenticationChecks(user, result);
        result.setDetails(authentication.getDetails());
        return result;
    }

    protected void additionalAuthenticationChecks(UserDetails userDetails, CustomIdPasswordAuthToken authentication) throws JwtException {
        log.debug("additionalAuthenticationChecks authentication = {}", authentication);
        if (authentication.getCredentials() == null) {
            log.debug("additionalAuthenticationChecks is null !");
            throw new MemberException(MemberExceptionType.NOT_FOUND_PASSWORD);
        }

        String presentedPassword = authentication.getCredentials().toString();
        log.debug("authentication.presentedPassword = {}", presentedPassword);

        if(!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            throw new MemberException(MemberExceptionType.WRONG_PASSWORD);
        }
    }

    protected final UserDetails retrieveUser(String username) throws JwtException {
        try {
            UserDetails loadedUser = customUserDetailsService.loadUserByUsername(username);
            if(loadedUser == null) {
                log.debug("user is null");
                throw new AuthException(AuthorityExceptionType.INTERNAL_AUTH_EXCEPTION);
            }
            return loadedUser;
        } catch (JwtException e) {
            log.debug("error in retrieveUser = {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.debug("error in internal auth_exception = {}", e.getMessage());
            throw new AuthException(AuthorityExceptionType.INTERNAL_AUTH_EXCEPTION);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(CustomIdPasswordAuthToken.class);
    }
}
