package com.caubogeo.bogeo.domain.member;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Gender {
    WOMAN("WOMAN"),
    MAN("MAN");

    private String gender;

    Gender(String gender) {
        this.gender = gender;
    }
}