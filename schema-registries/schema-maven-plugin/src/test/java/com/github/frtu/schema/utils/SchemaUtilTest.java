package com.github.frtu.schema.utils;

import org.apache.avro.Schema;
import org.junit.Assert;
import org.junit.Test;
import tests.pojo.UserImpl;
import tests.pojo.UserInterface;

public class SchemaUtilTest {
    @Test
    public void genAvroSchemaFromInterface() {
        final Schema avroSchema = SchemaUtil.genAvroSchemaFrom(UserInterface.class);

        Assert.assertEquals("UserInterface", avroSchema.getName());
        Assert.assertEquals("tests.pojo", avroSchema.getNamespace());
        Assert.assertEquals(Schema.Type.RECORD, avroSchema.getType());

        final Schema.Field favoriteColor = avroSchema.getField("favoriteColor");
        Assert.assertEquals("favoriteColor", favoriteColor.name());
        Assert.assertEquals(Schema.Type.UNION, favoriteColor.schema().getType());

        final Schema.Field favoriteNumber = avroSchema.getField("favoriteNumber");
        Assert.assertEquals("favoriteNumber", favoriteNumber.name());
        Assert.assertEquals(Schema.Type.UNION, favoriteNumber.schema().getType());

        final Schema.Field name = avroSchema.getField("name");
        Assert.assertEquals("name", name.name());
        Assert.assertEquals(Schema.Type.UNION, name.schema().getType());
    }

    @Test
    public void genAvroSchemaFromImplementation() {
        final Schema avroSchema = SchemaUtil.genAvroSchemaFrom(UserImpl.class);

        Assert.assertEquals("UserImpl", avroSchema.getName());
        Assert.assertEquals("tests.pojo", avroSchema.getNamespace());
        Assert.assertEquals(Schema.Type.RECORD, avroSchema.getType());

        final Schema.Field favoriteColor = avroSchema.getField("favoriteColor");
        Assert.assertEquals("favoriteColor", favoriteColor.name());
        Assert.assertEquals(Schema.Type.UNION, favoriteColor.schema().getType());

        final Schema.Field favoriteNumber = avroSchema.getField("favoriteNumber");
        Assert.assertEquals("favoriteNumber", favoriteNumber.name());
        Assert.assertEquals(Schema.Type.UNION, favoriteNumber.schema().getType());

        final Schema.Field name = avroSchema.getField("name");
        Assert.assertEquals("name", name.name());
        Assert.assertEquals(Schema.Type.UNION, name.schema().getType());
    }
}