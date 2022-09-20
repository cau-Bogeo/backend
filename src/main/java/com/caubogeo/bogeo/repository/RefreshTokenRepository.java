package com.caubogeo.bogeo.repository;

import com.caubogeo.bogeo.domain.jwt.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByKey(String key);
    boolean existsByValue(String token);
}
