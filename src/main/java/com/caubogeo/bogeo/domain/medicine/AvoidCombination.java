package com.caubogeo.bogeo.domain.medicine;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Table(name="combination")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class AvoidCombination {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column
    private Long id;

    @NotNull
    @Column
    private String firstMedicineSeq;

    @NotNull
    @Column(length = 600)
    private String firstMedicineName;

    @NotNull
    @Column
    private String secondMedicineSeq;

    @NotNull
    @Column(length = 600)
    private String secondMedicineName;

    @Column(length = 600)
    private String prohibitedContent;
}