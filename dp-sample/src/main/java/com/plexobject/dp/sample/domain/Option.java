package com.plexobject.dp.sample.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Option extends Security {
    private OptionRoot optionRoot;
    private OptionType optionType;
    private BigDecimal strikePrice;
    private Date expirationDate;

    public Option() {

    }

    public Option(long securityId, String exchange, String symbol,
            String underlyingSymbol, String description, SecurityType type,
            Beta beta, OptionRoot optionRoot, OptionType optionType,
            BigDecimal strikePrice, Date expirationDate) {
        super(securityId, exchange, symbol, underlyingSymbol, description,
                type, beta);
        this.optionRoot = optionRoot;
        this.optionType = optionType;
        this.strikePrice = strikePrice;
        this.expirationDate = expirationDate;
    }

    public OptionRoot getOptionRoot() {
        return optionRoot;
    }

    public void setOptionRoot(OptionRoot optionRoot) {
        this.optionRoot = optionRoot;
    }

    public OptionType getOptionType() {
        return optionType;
    }

    public void setOptionType(OptionType optionType) {
        this.optionType = optionType;
    }

    public BigDecimal getStrikePrice() {
        return strikePrice;
    }

    public void setStrikePrice(BigDecimal strikePrice) {
        this.strikePrice = strikePrice;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

}
