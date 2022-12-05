package com.caubogeo.bogeo.repository;

import com.caubogeo.bogeo.domain.medicine.CustomMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomMedicineRepository extends JpaRepository<CustomMedicine, Long> {

}
