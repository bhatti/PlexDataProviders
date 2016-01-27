package com.plexobject.dp;

public final class InitialValue {
    public static final InitialValue instance = new InitialValue();

    private InitialValue() {
    }

    @Override
    public String toString() {
        return "InitialValue";
    }

}
