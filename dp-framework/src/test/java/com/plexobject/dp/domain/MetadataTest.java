package com.plexobject.dp.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class MetadataTest {

    @Test
    public void testCreateEmpty() {
        Metadata fields = new Metadata();
        assertEquals(0, fields.size());
    }

    @Test
    public void testCreateVector() {
        MetaField field1 = MetaFieldFactory.createText("name");
        MetaField field2 = MetaFieldFactory.createText("phone");
        Metadata fields = new Metadata(field1, field2);
        assertEquals(2, fields.size());
        assertTrue(fields.getMetaFields().contains(field1));
        assertTrue(fields.getMetaFields().contains(field2));
    }

    @Test
    public void testCreateCollection() {
        MetaField field1 = MetaFieldFactory.createText("name");
        MetaField field2 = MetaFieldFactory.createText("phone");
        Metadata fields = new Metadata(Arrays.asList(field1, field2));
        assertEquals(2, fields.size());
        assertTrue(fields.getMetaFields().contains(field1));
        assertTrue(fields.getMetaFields().contains(field2));
    }

    @Test
    public void testAddMetaField() {
        Metadata fields = new Metadata();
        MetaField field1 = MetaFieldFactory.createText("name");
        MetaField field2 = MetaFieldFactory.createText("phone");
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
        MetaField field1 = MetaFieldFactory.createText("name");
        MetaField field2 = MetaFieldFactory.createText("phone");
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
        MetaField field1a = MetaFieldFactory.createText("name");
        MetaField field1b = MetaFieldFactory.createText("phone");
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
        MetaField field1a = MetaFieldFactory.createText("name");
        MetaField field1b = MetaFieldFactory.createText("phone");
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
        MetaField field1a = MetaFieldFactory.createText("name");
        MetaField field1b = MetaFieldFactory.createText("phone");
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
        MetaField field1 = MetaFieldFactory.createText("name");
        MetaField field2 = MetaFieldFactory.createText("phone");
        MetaField field3 = MetaFieldFactory.createText("address");

        Metadata fields1 = Metadata.from(field1, field2);
        Metadata fields2 = Metadata.from(field1, field2, field3);
        //
        assertEquals(1, fields1.getMissingCount(fields2));
        Metadata missing = fields1.getMissingMetadata(fields2);
        assertEquals(1, missing.size());
    }

    @Test
    public void testGetMatchingCount() {
        MetaField field1 = MetaFieldFactory.createText("name");
        MetaField field2 = MetaFieldFactory.createText("phone");
        MetaField field3 = MetaFieldFactory.createText("address");

        Metadata fields1 = Metadata.from(field1, field2);
        Metadata fields2 = Metadata.from(field1, field2, field3);
        //
        assertEquals(2, fields1.getMatchingCount(fields2));
    }

    @Test
    public void testOf() {
        MetaField field1 = MetaFieldFactory.createText("name");
        MetaField field2 = MetaFieldFactory.createText("phone");
        Metadata fields = Metadata.from(field1, field2);
        assertEquals(2, fields.size());
        assertTrue(fields.getMetaFields().contains(field1));
        assertTrue(fields.getMetaFields().contains(field2));
    }

    @Test
    public void testOfString() {
        MetaField field1 = MetaFieldFactory.createText("name");
        MetaField field2 = MetaFieldFactory.createText("phone");
        Metadata fields = Metadata.from(field1, field2);
        assertEquals(2, fields.size());
    }

    @Test
    public void testHashcode() {
        MetaField field1 = MetaFieldFactory.createText("name");
        MetaField field2 = MetaFieldFactory.createText("phone");
        Metadata fields = Metadata.from(field1, field2);
        assertTrue(fields.hashCode() != 0);
    }

    @Test
    public void testEquals() {
        MetaField field1 = MetaFieldFactory.createText("name");
        MetaField field2 = MetaFieldFactory.createText("phone");
        MetaField field3 = MetaFieldFactory.createText("address");

        Metadata fields1 = Metadata.from(field1, field2);
        Metadata fields2 = Metadata.from(field1, field2);
        Metadata fields3 = Metadata.from(field1, field3);
        Metadata fields4 = Metadata.from(field1, field2, field3);

        assertEquals(fields1, fields1);
        assertEquals(fields1, fields2);
        assertNotEquals(fields1, fields3);
        assertNotEquals(fields1, fields4);
        assertNotEquals(fields1, 1);
        assertNotEquals(fields1, null);
    }

    @Test
    public void testToString() {
        MetaField field1 = MetaFieldFactory.createText("name");
        MetaField field2 = MetaFieldFactory.createText("phone");
        Metadata fields = Metadata.from(field1, field2);
        assertTrue(fields.toString().contains("name"));
    }
}
