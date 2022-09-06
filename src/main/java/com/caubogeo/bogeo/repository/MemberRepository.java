package com.caubogeo.bogeo.repository;

import com.caubogeo.bogeo.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(String id);
    boolean existsById(String id);
}
