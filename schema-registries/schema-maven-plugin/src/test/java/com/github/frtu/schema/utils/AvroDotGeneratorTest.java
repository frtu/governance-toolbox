package com.github.frtu.schema.utils;

import org.apache.avro.Schema;
import org.junit.Test;
import tests.pojo.complex.ComplexStructureParent;

public class AvroDotGeneratorTest {

    @Test
    public void generateDot() {
        final Schema avroSchema = SchemaUtil.genAvroSchemaFrom(ComplexStructureParent.class);
        System.out.println(avroSchema.toString(true));

        final String graphName = avroSchema.getFullName();
        final AvroDotGenerator avroDotGenerator = new AvroDotGenerator(graphName);
        final String result = avroDotGenerator.generateGraph(avroSchema);
        System.out.println(result);
    }
}