package com.github.frtu.kafka.serdes;

import com.github.frtu.serdes.avro.DummyData;

public class DummyDataKafkaAvroRecordDeserializer extends KafkaAvroRecordDeserializer<DummyData> {
    public DummyDataKafkaAvroRecordDeserializer() {
        super(DummyData.getClassSchema());
    }
}
