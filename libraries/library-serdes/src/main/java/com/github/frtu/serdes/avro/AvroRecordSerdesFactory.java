package com.github.frtu.serdes.avro;

import org.apache.avro.generic.GenericContainer;

public interface AvroRecordSerdesFactory<T extends GenericContainer> {
    
    AvroRecordSerializer<T> buildSerializer();

    AvroRecordSerializer<T> buildSerializer(boolean isFormatJson);

    AvroRecordDeserializer<T> buildDeserializer();

    AvroRecordDeserializer<T> buildDeserializer(boolean isFormatJson);
}
