package com.github.frtu.schema.utils;

import org.apache.avro.Schema;
import org.junit.Test;
import tests.pojo.complex.ComplexStructureParent;

import static org.junit.Assert.*;

public class Avro2StructTypeGeneratorTest {

    @Test
    public void generateDot() {
        final Schema avroSchema = SchemaUtil.genAvroSchemaFrom(ComplexStructureParent.class);
        System.out.println(avroSchema.toString(true));

        final Avro2StructTypeGenerator avro2StructTypeGenerator = new Avro2StructTypeGenerator();
        final String result = avro2StructTypeGenerator.generateStructType(avroSchema);
        System.out.println(result);
    }
}