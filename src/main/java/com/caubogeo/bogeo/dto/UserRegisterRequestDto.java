package com.caubogeo.bogeo.dto;

import com.caubogeo.bogeo.domain.member.Gender;
import com.caubogeo.bogeo.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRegisterRequestDto {
    private String userId;
    private String password;
    private Gender gender;
    private int age;

    @Builder
    public UserRegisterRequestDto(Member user) {
        this.userId = user.getId();
        this.password = user.getPassword();
        this.gender = user.getGender();
        this.age = user.getAge();
    }

    public Member toEntity() {
        return Member.builder()
                .id(userId)
                .age(age)
                .password(password)
                .gender(gender)
                .build();
    }
}
