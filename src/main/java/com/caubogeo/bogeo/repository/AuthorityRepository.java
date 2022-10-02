package com.caubogeo.bogeo.repository;

import com.caubogeo.bogeo.domain.auth.Authority;
import com.caubogeo.bogeo.domain.auth.MemberAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {
    Optional<Authority> findByAuthorityName(MemberAuth authorityName);
}
