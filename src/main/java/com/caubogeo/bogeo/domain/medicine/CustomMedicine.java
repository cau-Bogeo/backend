package com.caubogeo.bogeo.domain.medicine;

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
import org.jetbrains.annotations.NotNull;

@Table(name="custom_medicine")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class CustomMedicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    private Long memberId;

    @Column
    private String medicineName;

    @Column
    private String medicineImageUrl;
}
