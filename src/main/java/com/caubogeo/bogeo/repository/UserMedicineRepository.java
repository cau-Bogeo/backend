package com.caubogeo.bogeo.repository;

import com.caubogeo.bogeo.domain.member.Medicine;
import com.caubogeo.bogeo.domain.member.Member;
import com.caubogeo.bogeo.dto.member.UserMedicineQuery;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMedicineRepository extends JpaRepository<Medicine, Long> {
    List<UserMedicineQuery> findByUser(Member user);
}
