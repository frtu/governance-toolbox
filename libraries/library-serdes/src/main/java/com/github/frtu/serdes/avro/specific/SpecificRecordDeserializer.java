package com.github.frtu.serdes.avro.specific;

import com.github.frtu.serdes.avro.AvroRecordDeserializer;
import org.apache.avro.Schema;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecord;

/**
 * An Avro record deserializer for POJO
 *
 * @param <T> The specific Avro class it is meant to deserialize
 * @author frtu
 */
public class SpecificRecordDeserializer<T extends SpecificRecord> extends AvroRecordDeserializer<T> {

    public SpecificRecordDeserializer(T similarRecord) {
        this(similarRecord.getSchema());
    }

    public SpecificRecordDeserializer(Schema schema) {
        super(schema);
    }

    @Override
    protected DatumReader<T> buildDatumReader() {
        return new SpecificDatumReader<>(getSchema());
    }
}
