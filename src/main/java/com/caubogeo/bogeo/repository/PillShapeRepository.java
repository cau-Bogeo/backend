package com.caubogeo.bogeo.repository;

import com.caubogeo.bogeo.domain.medicine.PillShape;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PillShapeRepository extends JpaRepository<PillShape, Long> {
}
