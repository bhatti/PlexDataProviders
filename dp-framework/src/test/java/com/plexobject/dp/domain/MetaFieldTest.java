package com.plexobject.dp.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class MetaFieldTest {

    @Test
    public void testCreate() {
        MetaField field = MetaFieldFactory.create("name", MetaFieldType.SCALAR_TEXT);
        assertEquals("name", field.getName());
        assertEquals(MetaFieldType.SCALAR_TEXT, field.getType());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateWithDifferentType() {
        MetaFieldFactory.create("name", MetaFieldType.SCALAR_TEXT);
        MetaFieldFactory.create("name", MetaFieldType.SCALAR_DATE);
    }

    @Test
    public void testHashCode() {
        MetaField field = MetaFieldFactory.create("name", MetaFieldType.SCALAR_TEXT);
        assertTrue(field.hashCode() != 0);
    }

    @Test
    public void testToString() {
        MetaField field = MetaFieldFactory.create("phone", MetaFieldType.SCALAR_TEXT);
        assertTrue(field.toString().contains("phone"));
    }

    @Test
    public void testEquals() {
        MetaField field1 = MetaFieldFactory.create("name", MetaFieldType.SCALAR_TEXT);
        MetaField field2 = MetaFieldFactory.create("name", MetaFieldType.SCALAR_TEXT);
        MetaField field3 = MetaFieldFactory.create("address",
                MetaFieldType.SCALAR_TEXT);
        assertEquals(field1, field1);
        assertEquals(field1, field2);
        assertNotEquals(field1, field3);
        assertNotEquals(field1, null);
        assertNotEquals(field1, 1);
    }

    @Test
    public void testCompare() {
        MetaField field1 = MetaFieldFactory.create("name", MetaFieldType.SCALAR_TEXT);
        MetaField field2 = MetaFieldFactory.create("phone", MetaFieldType.SCALAR_TEXT);
        assertTrue(field1.compareTo(field2) < 0);
    }

}
