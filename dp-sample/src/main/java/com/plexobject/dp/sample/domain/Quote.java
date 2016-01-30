package com.plexobject.dp.sample.domain;

import java.math.BigDecimal;

public class Quote {
    private String symbol;
    private String instrumentType;
    private BigDecimal bidPrice = BigDecimal.ZERO;
    private BigDecimal askPrice = BigDecimal.ZERO;
    private BigDecimal closePrice = BigDecimal.ZERO;
    private BigDecimal tradePrice = BigDecimal.ZERO;
    private BigDecimal markPrice = BigDecimal.ZERO;
    private BigDecimal volume = BigDecimal.ZERO;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }

    public BigDecimal getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(BigDecimal askPrice) {
        this.askPrice = askPrice;
    }

    public BigDecimal getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(BigDecimal closePrice) {
        this.closePrice = closePrice;
    }

    public BigDecimal getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(BigDecimal tradePrice) {
        this.tradePrice = tradePrice;
    }

    public BigDecimal getMarkPrice() {
        return markPrice;
    }

    public void setMarkPrice(BigDecimal markPrice) {
        this.markPrice = markPrice;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

}
