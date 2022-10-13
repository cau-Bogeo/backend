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
    private String first_medicine_seq;

    @NotNull
    @Column(length = 600)
    private String first_medicine_name;

    @NotNull
    @Column
    private String second_medicine_seq;

    @NotNull
    @Column(length = 600)
    private String second_medicine_name;

    @Column(length = 600)
    private String prohibited_content;
}