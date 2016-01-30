package com.plexobject.dp.sample.domain;

import java.math.BigDecimal;

public class Portfolio {
    private Account account;
    private BigDecimal cash;
    private BigDecimal margin;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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
