package com.plexobject.dp.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class MetaFieldsTest {

    @Test
    public void testCreateEmpty() {
        MetaFields fields = new MetaFields();
        assertEquals(0, fields.size());
    }

    @Test
    public void testCreateArray() {
        MetaField field1 = MetaFieldFactory.create("name", MetaFieldType.TEXT);
        MetaField field2 = MetaFieldFactory.create("phone", MetaFieldType.TEXT);
        MetaFields fields = new MetaFields(field1, field2);
        assertEquals(2, fields.size());
        assertTrue(fields.getMetaFields().contains(field1));
        assertTrue(fields.getMetaFields().contains(field2));
    }

    @Test
    public void testCreateCollection() {
        MetaField field1 = MetaFieldFactory.create("name", MetaFieldType.TEXT);
        MetaField field2 = MetaFieldFactory.create("phone", MetaFieldType.TEXT);
        MetaFields fields = new MetaFields(Arrays.asList(field1, field2));
        assertEquals(2, fields.size());
        assertTrue(fields.getMetaFields().contains(field1));
        assertTrue(fields.getMetaFields().contains(field2));
    }

    @Test
    public void testAddMetaField() {
        MetaFields fields = new MetaFields();
        MetaField field1 = MetaFieldFactory.create("name", MetaFieldType.TEXT);
        MetaField field2 = MetaFieldFactory.create("phone", MetaFieldType.TEXT);
        fields.addMetaField(field1);
        fields.addMetaField(field1);
        fields.addMetaField(field2);
        assertEquals(2, fields.size());
        assertTrue(fields.getMetaFields().contains(field1));
        assertTrue(fields.getMetaFields().contains(field2));
    }

    @Test
    public void testRemoveMetaField() {
        MetaFields fields = new MetaFields();
        MetaField field1 = MetaFieldFactory.create("name", MetaFieldType.TEXT);
        MetaField field2 = MetaFieldFactory.create("phone", MetaFieldType.TEXT);
        fields.addMetaField(field1);
        fields.addMetaField(field2);
        assertEquals(2, fields.size());
        assertTrue(fields.getMetaFields().contains(field1));
        assertTrue(fields.getMetaFields().contains(field2));
        fields.removeMetaField(field1);
        assertEquals(1, fields.size());
        assertFalse(fields.getMetaFields().contains(field1));
        assertTrue(fields.getMetaFields().contains(field2));
    }

    @Test
    public void testAddMetaFields() {
        MetaFields fields1 = new MetaFields();
        MetaField field1a = MetaFieldFactory.create("name", MetaFieldType.TEXT);
        MetaField field1b = MetaFieldFactory
                .create("phone", MetaFieldType.TEXT);
        fields1.addMetaField(field1a);
        fields1.addMetaField(field1b);
        MetaFields fields2 = new MetaFields();
        fields2.addMetaFields(fields1);
        assertEquals(2, fields2.size());
        fields2.getMetaFields().contains(field1a);
        fields2.getMetaFields().contains(field1b);

    }

    @Test
    public void testRemoveMetaFields() {
        MetaFields fields1 = new MetaFields();
        MetaField field1a = MetaFieldFactory.create("name", MetaFieldType.TEXT);
        MetaField field1b = MetaFieldFactory
                .create("phone", MetaFieldType.TEXT);
        fields1.addMetaField(field1a);
        fields1.addMetaField(field1b);
        MetaFields fields2 = new MetaFields();
        fields2.addMetaFields(fields1);
        assertEquals(2, fields2.size());
        fields2.getMetaFields().contains(field1a);
        fields2.getMetaFields().contains(field1b);

        fields2.removeMetaFields(fields1);
        assertEquals(0, fields2.size());
    }

    @Test
    public void testContains() {
        MetaFields fields1 = new MetaFields();
        MetaField field1a = MetaFieldFactory.create("name", MetaFieldType.TEXT);
        MetaField field1b = MetaFieldFactory
                .create("phone", MetaFieldType.TEXT);
        fields1.addMetaField(field1a);
        fields1.addMetaField(field1b);
        MetaFields fields2 = new MetaFields();
        fields2.addMetaFields(fields1);
        assertTrue(fields1.containsAll(fields2));
        fields1.removeMetaField(field1a);
        assertFalse(fields1.containsAll(fields2));
        //
        assertTrue(fields1.contains(field1b));
        assertFalse(fields1.contains(field1a));
    }

    @Test
    public void testGetMissingCount() {
        MetaFields fields1 = MetaFields.fromRaw("name", "TEXT", "phone", "TEXT");
        MetaFields fields2 = MetaFields.fromRaw("name", "TEXT", "phone", "TEXT",
                "address", "TEXT");
        //
        assertEquals(1, fields1.getMissingCount(fields2));
        MetaFields missing = fields1.getMissingMetaFields(fields2);
        assertEquals(1, missing.size());
    }

    @Test
    public void testOf() {
        MetaField field1 = MetaFieldFactory.create("name", MetaFieldType.TEXT);
        MetaField field2 = MetaFieldFactory.create("phone", MetaFieldType.TEXT);
        MetaFields fields = MetaFields.from(field1, field2);
        assertEquals(2, fields.size());
        assertTrue(fields.getMetaFields().contains(field1));
        assertTrue(fields.getMetaFields().contains(field2));
    }

    @Test
    public void testOfString() {
        MetaFields fields = MetaFields.fromRaw("name", "TEXT", "phone", "TEXT");
        assertEquals(2, fields.size());
    }

    @Test
    public void testHashcode() {
        MetaFields fields = MetaFields.fromRaw("name", "TEXT", "phone", "TEXT");
        assertTrue(fields.hashCode() != 0);
    }

    @Test
    public void testEquals() {
        MetaFields fields1 = MetaFields.fromRaw("name", "TEXT", "phone", "TEXT");
        MetaFields fields2 = MetaFields.fromRaw("name", "TEXT", "phone", "TEXT");
        MetaFields fields3 = MetaFields.fromRaw("name", "TEXT", "address", "TEXT");
        MetaFields fields4 = MetaFields.fromRaw("name", "TEXT", "address", "TEXT", "phone", "TEXT");
        assertEquals(fields1, fields1);
        assertEquals(fields1, fields2);
        assertNotEquals(fields1, fields3);
        assertNotEquals(fields1, fields4);
        assertNotEquals(fields1, 1);
        assertNotEquals(fields1, null);
    }

    @Test
    public void testToString() {
        MetaFields fields = MetaFields.fromRaw("name", "TEXT", "phone", "TEXT");
        assertTrue(fields.toString().contains("name"));
    }
}
