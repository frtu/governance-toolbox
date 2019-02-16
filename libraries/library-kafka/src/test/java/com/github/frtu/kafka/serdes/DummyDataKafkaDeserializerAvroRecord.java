package com.github.frtu.kafka.serdes;

import com.github.frtu.serdes.avro.DummyData;

public class DummyDataKafkaDeserializerAvroRecord extends KafkaDeserializerAvroRecord<DummyData> {
    public DummyDataKafkaDeserializerAvroRecord() {
        super(DummyData.getClassSchema());
    }
}
