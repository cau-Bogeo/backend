package com.caubogeo.bogeo.repository;

import com.caubogeo.bogeo.domain.member.Medicine;
import com.caubogeo.bogeo.domain.member.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findByUser(Member user);
    Optional<Medicine> findById(Long id);
}
