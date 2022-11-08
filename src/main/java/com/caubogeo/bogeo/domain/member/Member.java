package com.caubogeo.bogeo.domain.member;

import com.caubogeo.bogeo.domain.BaseTimeEntity;
import com.caubogeo.bogeo.domain.auth.Authority;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
public class Member extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    @Id
    private Long memberId;

    @NotNull
    @Column
    private String id;

    @NotNull
    @Column
    private String password;

    @NotNull
    @Column
    private int age;

    @NotNull
    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @JsonIgnore
    @Column(name="activated")
    private boolean activated;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="member_authority",
            joinColumns = {@JoinColumn(name="member_id", referencedColumnName = "member_id")},
            inverseJoinColumns = {@JoinColumn(name="authority_name", referencedColumnName = "authority_name")}
    )
    private Set<Authority> authorities = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<Medicine> medicines = new ArrayList<>();
}
