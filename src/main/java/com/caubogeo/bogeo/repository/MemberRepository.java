package com.caubogeo.bogeo.repository;

import com.caubogeo.bogeo.domain.member.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(String id);
    boolean existsById(String id);
}
