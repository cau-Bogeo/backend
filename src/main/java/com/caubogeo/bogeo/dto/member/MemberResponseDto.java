package com.caubogeo.bogeo.dto.member;

import com.caubogeo.bogeo.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
    private String id;

    public static MemberResponseDto of(Member member) {
        return new MemberResponseDto(member.getId());
    }
}
