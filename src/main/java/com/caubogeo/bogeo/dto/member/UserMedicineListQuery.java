package com.caubogeo.bogeo.dto.member;

import com.caubogeo.bogeo.domain.member.Medicine;
import java.util.List;

public interface UserMedicineListQuery {
    List<Medicine> getMedicines();
}
