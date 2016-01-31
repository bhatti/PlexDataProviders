package com.plexobject.dp.sample.domain;

import java.math.BigDecimal;

public class Commission {
    private CommissionType commissionType;
    private BigDecimal commisionAmount;

    public Commission() {

    }

    public Commission(CommissionType commissionType, BigDecimal commisionAmount) {
        this.commissionType = commissionType;
        this.commisionAmount = commisionAmount;
    }

    public CommissionType getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(CommissionType commissionType) {
        this.commissionType = commissionType;
    }

    public BigDecimal getCommisionAmount() {
        return commisionAmount;
    }

    public void setCommisionAmount(BigDecimal commisionAmount) {
        this.commisionAmount = commisionAmount;
    }

}
