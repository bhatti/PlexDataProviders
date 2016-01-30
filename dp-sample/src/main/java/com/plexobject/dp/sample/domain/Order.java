package com.plexobject.dp.sample.domain;

import java.math.BigDecimal;
import java.security.Security;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class Order {
    private Date date = new Date();
    private long orderId;
    private Account account;
    private Security security;
    private String exchange;
    private Collection<OrderLeg> orderLegs = new HashSet<OrderLeg>();
    private OrderStatus status;
    private MarketSession marketSession;
    private PriceType priceType;
    private BigDecimal price;
    private BigDecimal quantity;
    private Date fillDate;
    private BigDecimal fillPrice;
    private BigDecimal fillQuantity;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public Collection<OrderLeg> getOrderLegs() {
        return orderLegs;
    }

    public void setOrderLegs(Collection<OrderLeg> orderLegs) {
        this.orderLegs = orderLegs;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public MarketSession getMarketSession() {
        return marketSession;
    }

    public void setMarketSession(MarketSession marketSession) {
        this.marketSession = marketSession;
    }

    public PriceType getPriceType() {
        return priceType;
    }

    public void setPriceType(PriceType priceType) {
        this.priceType = priceType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Date getFillDate() {
        return fillDate;
    }

    public void setFillDate(Date fillDate) {
        this.fillDate = fillDate;
    }

    public BigDecimal getFillPrice() {
        return fillPrice;
    }

    public void setFillPrice(BigDecimal fillPrice) {
        this.fillPrice = fillPrice;
    }

    public BigDecimal getFillQuantity() {
        return fillQuantity;
    }

    public void setFillQuantity(BigDecimal fillQuantity) {
        this.fillQuantity = fillQuantity;
    }

}
