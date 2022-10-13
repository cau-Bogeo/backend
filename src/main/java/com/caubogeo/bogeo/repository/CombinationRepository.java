package com.caubogeo.bogeo.repository;

import com.caubogeo.bogeo.domain.medicine.AvoidCombination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CombinationRepository extends JpaRepository<AvoidCombination, Long> {
}
