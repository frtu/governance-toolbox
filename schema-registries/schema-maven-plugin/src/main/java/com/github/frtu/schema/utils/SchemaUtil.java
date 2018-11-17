package com.github.frtu.schema.utils;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.avro.AvroFactory;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import com.fasterxml.jackson.dataformat.avro.schema.AvroSchemaGenerator;
import org.apache.avro.Schema;

/**
 * Allow to generate an Avro schema from a Class.
 *
 * @author frtu
 * @since 0.3.3
 */
public class SchemaUtil {
    /**
     * Generate a JSON containing the Avro Schema of the parameter Class.
     *
     * @param classInstance
     * @return
     */
    public static String genAvroSchemaStringFrom(Class<?> classInstance) {
        return genAvroSchemaStringFrom(classInstance, true);
    }

    /**
     * Generate a pretty printing JSON containing the Avro Schema of the parameter Class.
     *
     * @param classInstance
     * @param pretty
     * @return
     */
    public static String genAvroSchemaStringFrom(Class<?> classInstance, boolean pretty) {
        Schema avroSchema = genAvroSchemaFrom(classInstance);
        return avroSchema.toString(pretty);
    }

    /**
     * Generate an Avro Schema {@link Schema} of the parameter Class.
     *
     * @param classInstance
     * @return
     */
    public static Schema genAvroSchemaFrom(Class<?> classInstance) {
        ObjectMapper mapper = new ObjectMapper(new AvroFactory());
        AvroSchemaGenerator gen = new AvroSchemaGenerator();
        try {
            mapper.acceptJsonFormatVisitor(classInstance, gen);
        } catch (JsonMappingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        AvroSchema schemaWrapper = gen.getGeneratedSchema();

        return schemaWrapper.getAvroSchema();
    }
}
