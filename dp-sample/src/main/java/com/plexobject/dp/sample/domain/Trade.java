package com.plexobject.dp.sample.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Trade implements Idable<Long> {
    private Long id;
    private BigDecimal quantity;
    private BigDecimal price;
    private String exchange;
    private Date date;
    private Security security;

    public Trade() {

    }

    public Trade(Long id, BigDecimal quantity, BigDecimal price,
            String exchange, Date date, Security security) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.exchange = exchange;
        this.date = date;
        this.security = security;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

}
