package com.caubogeo.bogeo.domain.member;

import com.caubogeo.bogeo.domain.BaseTimeEntity;
import com.caubogeo.bogeo.domain.auth.Authority;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    @ManyToMany
    @JoinTable(
            name="member_authority",
            joinColumns = {@JoinColumn(name="id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name="authority_name", referencedColumnName = "authority_name")}
    )
    private Set<Authority> authorities = new HashSet<>();

    public void addAuthority(Authority authority) {
        this.getAuthorities().add(authority);
    }

    public void removeAuthority(Authority authority) {
        this.getAuthorities().remove(authority);
    }

    public void activate(boolean flag) {
        this.activated = flag;
    }

    public String getAuthoritiesToString() {
        return this.authorities.stream()
                .map(Authority::getAuthorityName)
                .collect(Collectors.joining(","));
    }
    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }
}
