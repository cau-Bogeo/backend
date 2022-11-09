package com.caubogeo.bogeo.repository;

import com.caubogeo.bogeo.domain.medicine.MedicineDetail;
import com.caubogeo.bogeo.dto.medicine.MedicineQuery;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineDetailRepository extends JpaRepository<MedicineDetail, Long> {

    List<MedicineQuery> findByItemNameContains(String itemName);
    MedicineDetail findByItemSeq(String itemSeq);
}
