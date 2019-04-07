package com.github.frtu.schema.utils;

import org.apache.avro.Schema;
import org.apache.spark.sql.types.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Allow to generate a StructType from an Avro schema
 *
 * @author frtu
 * @since 1.0.1
 */
public class Avro2StructTypeGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(Avro2StructTypeGenerator.class);

    private final static HashMap<Schema.Type, DataType> typeMappingToShape;

    static {
        typeMappingToShape = new HashMap<>();
        typeMappingToShape.put(Schema.Type.STRING, StringType$.MODULE$);
        typeMappingToShape.put(Schema.Type.BYTES, BinaryType$.MODULE$);
        typeMappingToShape.put(Schema.Type.INT, IntegerType$.MODULE$);
        typeMappingToShape.put(Schema.Type.LONG, LongType$.MODULE$);
        typeMappingToShape.put(Schema.Type.FLOAT, FloatType$.MODULE$);
        typeMappingToShape.put(Schema.Type.DOUBLE, DoubleType$.MODULE$);
        typeMappingToShape.put(Schema.Type.BOOLEAN, BooleanType$.MODULE$);
    }

    public String generateStructType(Schema avroSchema) {
        final StructType structType = new StructType();
        return structType.toString();
    }
}
