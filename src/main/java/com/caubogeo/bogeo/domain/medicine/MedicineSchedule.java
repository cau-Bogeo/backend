package com.caubogeo.bogeo.domain.medicine;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "medicine_schedule")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class MedicineSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @Column
    private Long medicineId;  // Medicine Id

    @Column
    private LocalDate scheduleDate;

    @Column
    private boolean isActivated;
}
