package com.plexobject.dp.sample.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Equity extends Security {
    private BigDecimal dividendRate;
    private String dividendInterval;
    private Date exDividendDate;
    private BigDecimal mpvValue;
    private BigDecimal betaValue;

    public BigDecimal getDividendRate() {
        return dividendRate;
    }

    public void setDividendRate(BigDecimal dividendRate) {
        this.dividendRate = dividendRate;
    }

    public String getDividendInterval() {
        return dividendInterval;
    }

    public void setDividendInterval(String dividendInterval) {
        this.dividendInterval = dividendInterval;
    }

    public Date getExDividendDate() {
        return exDividendDate;
    }

    public void setExDividendDate(Date exDividendDate) {
        this.exDividendDate = exDividendDate;
    }

    public BigDecimal getMpvValue() {
        return mpvValue;
    }

    public void setMpvValue(BigDecimal mpvValue) {
        this.mpvValue = mpvValue;
    }

    public BigDecimal getBetaValue() {
        return betaValue;
    }

    public void setBetaValue(BigDecimal betaValue) {
        this.betaValue = betaValue;
    }

}
