package com.plexobject.dp.sample.domain;

import java.math.BigDecimal;

public class Portfolio implements Idable<Long> {
    private long id;
    private BigDecimal cash;
    private BigDecimal margin;

    public Portfolio() {

    }

    public Portfolio(long id, BigDecimal cash, BigDecimal margin) {
        super();
        this.id = id;
        this.cash = cash;
        this.margin = margin;
    }

    public Long getId() {
        return id;
    }

    public void setId(long portfolioId) {
        this.id = portfolioId;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }

}
