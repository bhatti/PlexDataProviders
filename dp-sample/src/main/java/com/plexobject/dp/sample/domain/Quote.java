package com.plexobject.dp.sample.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Quote implements Idable<String> {
    private Security security;
    private BigDecimal bidPrice = BigDecimal.ZERO;
    private BigDecimal askPrice = BigDecimal.ZERO;
    private BigDecimal closePrice = BigDecimal.ZERO;
    private BigDecimal tradePrice = BigDecimal.ZERO;
    private BigDecimal markPrice = BigDecimal.ZERO;
    private BigDecimal volume = BigDecimal.ZERO;
    private MarketSession marketSession;
    private List<TimeOfSales> sales = new ArrayList<>();

    public Quote() {

    }

    public Quote(Security security, BigDecimal bidPrice, BigDecimal askPrice,
            BigDecimal closePrice, BigDecimal tradePrice, BigDecimal markPrice,
            BigDecimal volume, MarketSession marketSession,
            List<TimeOfSales> sales) {
        this.security = security;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
        this.closePrice = closePrice;
        this.tradePrice = tradePrice;
        this.markPrice = markPrice;
        this.volume = volume;
        this.marketSession = marketSession;
        this.sales = sales;
    }

    public String getId() {
        return security.getSymbol();
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

    public List<TimeOfSales> getSales() {
        return sales;
    }

    public void setSales(List<TimeOfSales> sales) {
        this.sales = sales;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public MarketSession getMarketSession() {
        return marketSession;
    }

    public void setMarketSession(MarketSession marketSession) {
        this.marketSession = marketSession;
    }

    @Override
    public String toString() {
        return "Quote(" + security.getSymbol() + ")";
    }

}
