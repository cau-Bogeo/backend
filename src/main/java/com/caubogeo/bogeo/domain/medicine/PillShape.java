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
    private String item_seq;

    @NotNull
    @Column
    private String item_name;

    @NotNull
    @Column
    private String entp_name;

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
    private List<String> color_front;

    @ElementCollection
    private List<String> color_rear;

    @Column
    private String class_name;

    @Column
    private String medicine_type;

    @Column
    private String print_front;

    @Column
    private String print_back;

    @ElementCollection
    private List<String> mark_front;

    @ElementCollection
    private List<String> mark_rear;
}
