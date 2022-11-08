package com.caubogeo.bogeo.domain.member;

import com.caubogeo.bogeo.domain.BaseTimeEntity;
import com.sun.istack.NotNull;
import java.time.LocalDateTime;
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

@Table
@Getter
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
    @NotNull
    private String medicineSeq;

    @Column
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private PeriodType periodType;

    @Column
    private String period;

    @Column
    private LocalDateTime endDay;

    @Column
    private int dosage;

    @Column
    private boolean isActivated;
}
