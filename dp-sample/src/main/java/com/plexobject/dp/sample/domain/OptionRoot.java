package com.plexobject.dp.sample.domain;

import java.math.BigDecimal;

public class OptionRoot {
    private String opraRoot;
    private Security underlying;
    private BigDecimal multiplier;
    private String excerciseStyle;

    public String getOpraRoot() {
        return opraRoot;
    }

    public void setOpraRoot(String opraRoot) {
        this.opraRoot = opraRoot;
    }

    public BigDecimal getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(BigDecimal multiplier) {
        this.multiplier = multiplier;
    }

    public String getExcerciseStyle() {
        return excerciseStyle;
    }

    public void setExcerciseStyle(String excerciseStyle) {
        this.excerciseStyle = excerciseStyle;
    }

    public Security getUnderlying() {
        return underlying;
    }

    public void setUnderlying(Security underlying) {
        this.underlying = underlying;
    }

}
