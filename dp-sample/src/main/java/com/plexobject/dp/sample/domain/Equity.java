package com.plexobject.dp.sample.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Equity extends Security {
    private BigDecimal dividendRate;
    private String dividendInterval;
    private Date exDividendDate;

    public Equity() {

    }

    public Equity(BigDecimal dividendRate, String dividendInterval,
            Date exDividendDate) {
        this.dividendRate = dividendRate;
        this.dividendInterval = dividendInterval;
        this.exDividendDate = exDividendDate;
    }

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

}
