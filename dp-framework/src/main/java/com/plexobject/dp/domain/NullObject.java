package com.plexobject.dp.domain;

/**
 * This class represents null object
 * 
 * @author shahzad bhatti
 *
 */
public final class NullObject {
    public static final NullObject instance = new NullObject();

    private NullObject() {
    }

    @Override
    public String toString() {
        return "NullObject";
    }

}
