package com.caubogeo.bogeo.dto.profile;

import com.caubogeo.bogeo.domain.member.Gender;
import com.caubogeo.bogeo.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponseDto {
    private String id;
    private int age;
    private Gender gender;

    public static ProfileResponseDto of(Member member) {
        return new ProfileResponseDto(member.getId(), member.getAge(), member.getGender());
    }
}
