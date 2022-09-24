package com.caubogeo.bogeo.service.profile;

import com.caubogeo.bogeo.domain.member.Member;
import com.caubogeo.bogeo.dto.profile.ProfileResponseDto;
import com.caubogeo.bogeo.exceptionhandler.MemberException;
import com.caubogeo.bogeo.exceptionhandler.MemberExceptionType;
import com.caubogeo.bogeo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {
    private final MemberRepository memberRepository;

    public ProfileResponseDto getMemberProfile(String id) {
        log.info("user id : {}", id);
        boolean isExistId = memberRepository.existsById(id);
        log.info("existByID : {}", isExistId);
        Optional<Member> member = memberRepository.findById(id);
        if(member.isPresent()) {
            return ProfileResponseDto.of(member.get());
        } else {
            throw new MemberException(MemberExceptionType.NOT_FOUND_USER);
        }
    }
}
