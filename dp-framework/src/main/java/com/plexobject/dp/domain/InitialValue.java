package com.plexobject.dp.domain;

/**
 * This class can be used to initialize data fields for row that are yet to be
 * populated
 * 
 * @author shahzad bhatti
 *
 */
public final class InitialValue {
    public static final InitialValue instance = new InitialValue();

    private InitialValue() {
    }

    @Override
    public String toString() {
        return "InitialValue";
    }

}
