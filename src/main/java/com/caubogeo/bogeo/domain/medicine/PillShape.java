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
public class PillShape {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @Id
    private Long id;

    @NotNull
    @Column
    private String itemSeq;

    @NotNull
    @Column
    private String itemName;

    @NotNull
    @Column
    private String entpName;

    @Column(length=500)
    private String chart;

    @NotNull
    @Column
    private String image;

    @NotNull
    @Column
    private String shape;

    @NotNull
    @ElementCollection
    private List<String> colorFront;

    @ElementCollection
    private List<String> colorRear;

    @Column
    private String className;

    @Column
    private String medicineType;

    @Column
    private String printFront;

    @Column
    private String printBack;

    @ElementCollection
    private List<String> markFront;

    @ElementCollection
    private List<String> markRear;
}
