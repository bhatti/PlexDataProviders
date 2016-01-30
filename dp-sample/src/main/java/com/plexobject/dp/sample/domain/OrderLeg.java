package com.plexobject.dp.sample.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderLeg {
    private OrderSide side;
    private BigDecimal price;
    private BigDecimal quantity;
    private BigDecimal fillPrice;
    private BigDecimal fillQuantity;
    private PositionType positionType;
    private String symbol;
    private List<Trade> trades = new ArrayList<>();

    public OrderSide getSide() {
        return side;
    }

    public void setSide(OrderSide side) {
        this.side = side;
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

    public PositionType getPositionType() {
        return positionType;
    }

    public void setPositionType(PositionType positionType) {
        this.positionType = positionType;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

}
