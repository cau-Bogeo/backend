package com.caubogeo.bogeo.repository;

import com.caubogeo.bogeo.domain.member.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMedicineRepository extends JpaRepository<Medicine, Long> {
}
