package com.plexobject.dp.sample.domain;

public class Security {
    private String exchange;
    private String symbol;
    private String underlyingSymbol;
    private String description;
    private SecurityType type;
    private Beta beta;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getUnderlyingSymbol() {
        return underlyingSymbol;
    }

    public void setUnderlyingSymbol(String underlyingSymbol) {
        this.underlyingSymbol = underlyingSymbol;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SecurityType getType() {
        return type;
    }

    public void setType(SecurityType type) {
        this.type = type;
    }

    public Beta getBeta() {
        return beta;
    }

    public void setBeta(Beta beta) {
        this.beta = beta;
    }

}
