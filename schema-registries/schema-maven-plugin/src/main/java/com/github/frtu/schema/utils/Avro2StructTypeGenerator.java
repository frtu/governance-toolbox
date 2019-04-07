package com.github.frtu.schema.utils;

import org.apache.avro.Schema;
import org.apache.spark.sql.types.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        final String namespace = avroSchema.getNamespace();
        final String schemaName = avroSchema.getName();
        LOGGER.info("Mapping schema namespace={} schemaName={}", namespace, schemaName);

        final List<StructField> structFields = new ArrayList<>();
        final List<Schema.Field> fields = avroSchema.getFields();

        fields.forEach(field -> {
            final String fieldName = field.name();
            boolean isNullable = false;

            final Schema schema = field.schema();
            final Schema.Type type = schema.getType();

            DataType dataType = typeMappingToShape.get(type);
            if (dataType == null) {
                if (Schema.Type.UNION.equals(type)) {
                    for (Schema schemaItem : schema.getTypes()) {
                        final Schema.Type schemaItemType = schemaItem.getType();

                        if (Schema.Type.NULL.equals(schemaItemType)) {
                            isNullable = true;
                        } else {
                            dataType = typeMappingToShape.get(schemaItemType);
                        }
                    }
                }
            }
            final StructField structField = new StructField(fieldName, dataType, isNullable, Metadata.empty());
            structField.withComment(field.doc());
            structFields.add(structField);
        });

        final StructType structType = new StructType(structFields.toArray(new StructField[structFields.size()]));
        return structType.toString();
    }
}
