package com.github.frtu.serdes.avro.generic;

import com.github.frtu.serdes.avro.AvroRecordDeserializer;
import com.github.frtu.serdes.avro.AvroRecordSerdesFactory;
import com.github.frtu.serdes.avro.AvroRecordSerializer;
import com.github.frtu.serdes.avro.DummyData;
import com.github.frtu.serdes.avro.specific.SpecificRecordDeserializer;
import com.github.frtu.serdes.avro.specific.SpecificRecordSerializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.specific.SpecificRecord;

public class GenericRecordSerdesFactory<T extends GenericRecord> implements AvroRecordSerdesFactory<T> {

    private Schema schema;

    private boolean isFormatJson;

    public GenericRecordSerdesFactory(Schema schema) {
        this(schema, false);
    }

    public GenericRecordSerdesFactory(Schema schema, boolean isFormatJson) {
        this.schema = schema;
        this.isFormatJson = isFormatJson;
    }

    @Override
    public AvroRecordSerializer<T> buildSerializer() {
        return buildSerializer(this.isFormatJson);
    }

    @Override
    public AvroRecordSerializer<T> buildSerializer(boolean isFormatJson) {
        return new GenericRecordSerializer(isFormatJson);
    }

    @Override
    public AvroRecordDeserializer<T> buildDeserializer() {
        return buildDeserializer(this.isFormatJson);
    }

    @Override
    public AvroRecordDeserializer<T> buildDeserializer(boolean isFormatJson) {
        return new GenericRecordDeserializer(this.schema, isFormatJson);
    }
}
