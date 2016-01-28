package com.plexobject.dp.domain;

public final class NullObject {
    public static final NullObject instance = new NullObject();

    private NullObject() {
    }

    @Override
    public String toString() {
        return "NullObject";
    }

}