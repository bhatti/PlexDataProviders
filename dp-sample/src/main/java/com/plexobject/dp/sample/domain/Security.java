package com.plexobject.dp.sample.domain;

public abstract class Security implements Idable<String> {
    private String exchange;
    private String symbol;
    private String underlyingSymbol;
    private String description;
    private SecurityType type;
    private Beta beta;

    public Security() {

    }

    public Security(String exchange, String symbol, String underlyingSymbol,
            String description, SecurityType type, Beta beta) {
        this.exchange = exchange;
        this.symbol = symbol;
        this.underlyingSymbol = underlyingSymbol;
        this.description = description;
        this.type = type;
        this.beta = beta;
    }

    public String getId() {
        return symbol;
    }

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Security other = (Security) obj;
        if (symbol == null) {
            if (other.symbol != null)
                return false;
        } else if (!symbol.equals(other.symbol))
            return false;
        return true;
    }

}
