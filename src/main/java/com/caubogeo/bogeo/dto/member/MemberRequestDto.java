package com.caubogeo.bogeo.dto.member;

import com.caubogeo.bogeo.domain.auth.Authority;
import com.caubogeo.bogeo.domain.member.Gender;
import com.caubogeo.bogeo.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberRequestDto {
    private String id;
    private String password;
    private int age;
    private Gender gender;

    public Member toMember(PasswordEncoder passwordEncoder, Set<Authority> authorities) {
        return Member.builder()
                .id(id)
                .password(passwordEncoder.encode(password))
                .age(age)
                .gender(gender)
                .activated(true)
                .authorities(authorities)
                .build();
    }
}
