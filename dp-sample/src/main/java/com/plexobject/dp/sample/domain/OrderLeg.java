package com.plexobject.dp.sample.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderLeg implements Idable<Long> {
    private long id;
    private OrderSide side;
    private BigDecimal price;
    private BigDecimal quantity;
    private BigDecimal fillPrice;
    private BigDecimal fillQuantity;
    private PositionType positionType;
    private List<Trade> trades = new ArrayList<>();

    public OrderLeg() {

    }

    public OrderLeg(long id, OrderSide side, BigDecimal price,
            BigDecimal quantity, BigDecimal fillPrice, BigDecimal fillQuantity,
            PositionType positionType, List<Trade> trades) {
        this.id = id;
        this.side = side;
        this.price = price;
        this.quantity = quantity;
        this.fillPrice = fillPrice;
        this.fillQuantity = fillQuantity;
        this.positionType = positionType;
        this.trades = trades;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

}
