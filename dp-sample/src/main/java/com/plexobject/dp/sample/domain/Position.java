package com.plexobject.dp.sample.domain;

import java.math.BigDecimal;

public class Position implements Idable<Long> {
    private long id;
    private Account account;
    private BigDecimal quantity;
    private BigDecimal price;
    private Security security;

    public Position() {

    }

    public Position(long id, Account account, BigDecimal quantity,
            BigDecimal price, Security security) {
        this.id = id;
        this.account = account;
        this.quantity = quantity;
        this.price = price;
        this.security = security;
    }

    public Long getId() {
        return id;
    }

    public void setId(long positionId) {
        this.id = positionId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

}
