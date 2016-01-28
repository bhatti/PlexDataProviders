package com.plexobject.dp.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class InitialValueTest {

    @Test
    public void testToString() {
        assertEquals("InitialValue", InitialValue.instance.toString());
    }
}
