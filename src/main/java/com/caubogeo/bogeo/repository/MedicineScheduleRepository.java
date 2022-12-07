package com.caubogeo.bogeo.repository;

import com.caubogeo.bogeo.domain.medicine.MedicineSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineScheduleRepository extends JpaRepository<MedicineSchedule, Long> {
}
