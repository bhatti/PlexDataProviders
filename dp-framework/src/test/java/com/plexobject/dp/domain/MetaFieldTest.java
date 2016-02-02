package com.plexobject.dp.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MetaFieldTest {

    @Test
    public void testCreate() {
        MetaField field = MetaFieldFactory.createText("name", "Test", false);
        assertEquals("name", field.getName());
        assertEquals(MetaFieldType.SCALAR_TEXT, field.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithDifferentType() {
        MetaFieldFactory.createText("name", "Test", false);
        MetaFieldFactory.createDate("name", "Test", false);
    }

    @Test
    public void testHashCode() {
        MetaField field = MetaFieldFactory.createText("name", "Test", false);
        assertTrue(field.hashCode() != 0);
    }

    @Test
    public void testToString() {
        MetaField field = MetaFieldFactory.createText("phone", "Test", false);
        assertTrue(field.toString().contains("phone"));
    }

    @Test
    public void testEquals() {
        MetaField field1 = MetaFieldFactory.createText("name", "Test", false);
        MetaField field2 = MetaFieldFactory.createText("name", "Test", false);
        MetaField field3 = MetaFieldFactory
                .createText("address", "Test", false);

        assertEquals(field1, field1);
        assertEquals(field1, field2);
        assertNotEquals(field1, field3);
        assertNotEquals(field1, null);
        assertNotEquals(field1, 1);
    }

    @Test
    public void testCompare() {
        MetaField field1 = MetaFieldFactory.createText("name", "Test", false);
        MetaField field2 = MetaFieldFactory.createText("phone", "Test", false);

        assertTrue(field1.compareTo(field2) < 0);
    }

}
