package com.caubogeo.bogeo.repository;

import com.caubogeo.bogeo.domain.medicine.MedicineDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineDetailRepository extends JpaRepository<MedicineDetail, Long> {
}
