package com.caubogeo.bogeo.domain.member;

import com.caubogeo.bogeo.domain.BaseTimeEntity;
import com.sun.istack.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Medicine extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @Id
    private Long id;

    @ManyToOne
    @NotNull
    private Member user;

    @Column
    private String medicineSeq;

    @Column
    private Long customMedicineId;

    @Column
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private PeriodType periodType;

    @Column
    private String period;

    @Column
    private boolean hasEndDay;

    @Column
    private LocalDate endDay;

    @Column
    private boolean hasMedicineTime;

    @Column(columnDefinition = "TIME")
    private LocalTime medicineTime;

    @Column
    private int dosage;

    @Column
    private boolean isActivated;
}
