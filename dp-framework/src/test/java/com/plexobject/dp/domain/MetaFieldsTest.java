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
        Metadata fields = new Metadata();
        assertEquals(0, fields.size());
    }

    @Test
    public void testCreateVector() {
        MetaField field1 = MetaFieldFactory.create("name", MetaFieldType.SCALAR_TEXT);
        MetaField field2 = MetaFieldFactory.create("phone", MetaFieldType.SCALAR_TEXT);
        Metadata fields = new Metadata(field1, field2);
        assertEquals(2, fields.size());
        assertTrue(fields.getMetaFields().contains(field1));
        assertTrue(fields.getMetaFields().contains(field2));
    }

    @Test
    public void testCreateCollection() {
        MetaField field1 = MetaFieldFactory.create("name", MetaFieldType.SCALAR_TEXT);
        MetaField field2 = MetaFieldFactory.create("phone", MetaFieldType.SCALAR_TEXT);
        Metadata fields = new Metadata(Arrays.asList(field1, field2));
        assertEquals(2, fields.size());
        assertTrue(fields.getMetaFields().contains(field1));
        assertTrue(fields.getMetaFields().contains(field2));
    }

    @Test
    public void testAddMetaField() {
        Metadata fields = new Metadata();
        MetaField field1 = MetaFieldFactory.create("name", MetaFieldType.SCALAR_TEXT);
        MetaField field2 = MetaFieldFactory.create("phone", MetaFieldType.SCALAR_TEXT);
        fields.addMetaField(field1);
        fields.addMetaField(field1);
        fields.addMetaField(field2);
        assertEquals(2, fields.size());
        assertTrue(fields.getMetaFields().contains(field1));
        assertTrue(fields.getMetaFields().contains(field2));
    }

    @Test
    public void testRemoveMetaField() {
        Metadata fields = new Metadata();
        MetaField field1 = MetaFieldFactory.create("name", MetaFieldType.SCALAR_TEXT);
        MetaField field2 = MetaFieldFactory.create("phone", MetaFieldType.SCALAR_TEXT);
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
        Metadata fields1 = new Metadata();
        MetaField field1a = MetaFieldFactory.create("name", MetaFieldType.SCALAR_TEXT);
        MetaField field1b = MetaFieldFactory
                .create("phone", MetaFieldType.SCALAR_TEXT);
        fields1.addMetaField(field1a);
        fields1.addMetaField(field1b);
        Metadata fields2 = new Metadata();
        fields2.addMetadata(fields1);
        assertEquals(2, fields2.size());
        fields2.getMetaFields().contains(field1a);
        fields2.getMetaFields().contains(field1b);

    }

    @Test
    public void testRemoveMetaFields() {
        Metadata fields1 = new Metadata();
        MetaField field1a = MetaFieldFactory.create("name", MetaFieldType.SCALAR_TEXT);
        MetaField field1b = MetaFieldFactory
                .create("phone", MetaFieldType.SCALAR_TEXT);
        fields1.addMetaField(field1a);
        fields1.addMetaField(field1b);
        Metadata fields2 = new Metadata();
        fields2.addMetadata(fields1);
        assertEquals(2, fields2.size());
        fields2.getMetaFields().contains(field1a);
        fields2.getMetaFields().contains(field1b);

        fields2.removeMetadata(fields1);
        assertEquals(0, fields2.size());
    }

    @Test
    public void testContains() {
        Metadata fields1 = new Metadata();
        MetaField field1a = MetaFieldFactory.create("name", MetaFieldType.SCALAR_TEXT);
        MetaField field1b = MetaFieldFactory
                .create("phone", MetaFieldType.SCALAR_TEXT);
        fields1.addMetaField(field1a);
        fields1.addMetaField(field1b);
        Metadata fields2 = new Metadata();
        fields2.addMetadata(fields1);
        assertTrue(fields1.containsAll(fields2));
        fields1.removeMetaField(field1a);
        assertFalse(fields1.containsAll(fields2));
        //
        assertTrue(fields1.contains(field1b));
        assertFalse(fields1.contains(field1a));
    }

    @Test
    public void testGetMissingCount() {
        Metadata fields1 = Metadata.fromRaw("name", "SCALAR_TEXT", "phone", "SCALAR_TEXT");
        Metadata fields2 = Metadata.fromRaw("name", "SCALAR_TEXT", "phone", "SCALAR_TEXT",
                "address", "SCALAR_TEXT");
        //
        assertEquals(1, fields1.getMissingCount(fields2));
        Metadata missing = fields1.getMissingMetadata(fields2);
        assertEquals(1, missing.size());
    }

    @Test
    public void testOf() {
        MetaField field1 = MetaFieldFactory.create("name", MetaFieldType.SCALAR_TEXT);
        MetaField field2 = MetaFieldFactory.create("phone", MetaFieldType.SCALAR_TEXT);
        Metadata fields = Metadata.from(field1, field2);
        assertEquals(2, fields.size());
        assertTrue(fields.getMetaFields().contains(field1));
        assertTrue(fields.getMetaFields().contains(field2));
    }

    @Test
    public void testOfString() {
        Metadata fields = Metadata.fromRaw("name", "SCALAR_TEXT", "phone", "SCALAR_TEXT");
        assertEquals(2, fields.size());
    }

    @Test
    public void testHashcode() {
        Metadata fields = Metadata.fromRaw("name", "SCALAR_TEXT", "phone", "SCALAR_TEXT");
        assertTrue(fields.hashCode() != 0);
    }

    @Test
    public void testEquals() {
        Metadata fields1 = Metadata.fromRaw("name", "SCALAR_TEXT", "phone", "SCALAR_TEXT");
        Metadata fields2 = Metadata.fromRaw("name", "SCALAR_TEXT", "phone", "SCALAR_TEXT");
        Metadata fields3 = Metadata.fromRaw("name", "SCALAR_TEXT", "address", "SCALAR_TEXT");
        Metadata fields4 = Metadata.fromRaw("name", "SCALAR_TEXT", "address", "SCALAR_TEXT", "phone", "SCALAR_TEXT");
        assertEquals(fields1, fields1);
        assertEquals(fields1, fields2);
        assertNotEquals(fields1, fields3);
        assertNotEquals(fields1, fields4);
        assertNotEquals(fields1, 1);
        assertNotEquals(fields1, null);
    }

    @Test
    public void testToString() {
        Metadata fields = Metadata.fromRaw("name", "SCALAR_TEXT", "phone", "SCALAR_TEXT");
        assertTrue(fields.toString().contains("name"));
    }
}
