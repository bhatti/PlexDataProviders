package com.plexobject.dp.sample.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Dividend {
    private long dividendId;
    private Date dividendDate;
    private BigDecimal dividendAmount;
    private Security security;

    public long getDividendId() {
        return dividendId;
    }

    public void setDividendId(long dividendId) {
        this.dividendId = dividendId;
    }

    public Date getDividendDate() {
        return dividendDate;
    }

    public void setDividendDate(Date dividendDate) {
        this.dividendDate = dividendDate;
    }

    public BigDecimal getDividendAmount() {
        return dividendAmount;
    }

    public void setDividendAmount(BigDecimal dividendAmount) {
        this.dividendAmount = dividendAmount;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

   
}
