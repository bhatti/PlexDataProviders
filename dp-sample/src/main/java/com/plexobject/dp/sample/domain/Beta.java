package com.plexobject.dp.sample.domain;

import java.math.BigDecimal;

public class Beta {
    private BigDecimal beta;
    private int month;

    public Beta() {

    }

    public Beta(BigDecimal beta, int month) {
        this.beta = beta;
        this.month = month;
    }

    public BigDecimal getBeta() {
        return beta;
    }

    public void setBeta(BigDecimal beta) {
        this.beta = beta;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

}
