package com.caubogeo.bogeo.domain.member;

import java.util.Arrays;
import lombok.Getter;

public enum PeriodType {
    WEEK_DAY(0),
    SPECIFIC_PERIOD(1),
    EVERY_DAY(2);
    @Getter
    private final int period;
    PeriodType(int period) {
        this.period = period;
    }

    public static PeriodType valueOfLabel(int period) {
        return Arrays.stream(values())
                .filter(value -> value.period == period)
                .findAny()
                .orElse(null);
    }
}
