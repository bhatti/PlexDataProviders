package com.plexobject.dp.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class NullObjectTest {

    @Test
    public void testToString() {
        assertEquals("NullObject", NullObject.instance.toString());
    }
}
