package com.caubogeo.bogeo.domain.member;

public enum PeriodType {
    WEEK_DAY(0),
    SPECIFIC_PERIOD(1),
    EVERY_DAY(2);
    private final int period;
    public int getPeriod() {
        return period;
    }
    PeriodType(int period) {
        this.period = period;
    }
}
