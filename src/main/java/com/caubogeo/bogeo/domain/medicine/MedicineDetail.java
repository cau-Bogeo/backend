package com.caubogeo.bogeo.domain.medicine;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class MedicineDetail {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @Id
    private Long id;

    @NotNull
    @Column
    private String itemSeq;

    @NotNull
    @Column(length = 600)
    private String itemName;

    @NotNull
    @Column
    private String entpName;

    @Column
    private String medicineCode;

    @Lob
    private String medicineEffect;

    @Lob
    private String medicineDosage;

    @Lob
    private String medicineWarning;

    @Column(length = 600)
    private String storageMethod;

    @ElementCollection
    private List<String> mainItemIngredient;

    @Column
    private String validTerm;

    @Column
    private String image;

    @Column
    private String className;
}
