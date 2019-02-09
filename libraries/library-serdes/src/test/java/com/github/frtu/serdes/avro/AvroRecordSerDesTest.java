package com.github.frtu.serdes.avro;

import com.github.frtu.serdes.avro.generic.GenericRecordDeserializer;
import com.github.frtu.serdes.avro.generic.GenericRecordSerializer;
import com.github.frtu.serdes.avro.specific.SpecificRecordDeserializer;
import com.github.frtu.serdes.avro.specific.SpecificRecordSerializer;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class AvroRecordSerDesTest {

    @Test
    public void specificSerdes() throws IOException {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        DummyData dummyData = DummyData.newBuilder().setName("Fred").build();

        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        AvroRecordSerializer<DummyData> specificRecordSerializer = new SpecificRecordSerializer<>();
        byte[] serializedBytes = specificRecordSerializer.serialize(dummyData);

        AvroRecordDeserializer<DummyData> specificRecordDeserializer = new SpecificRecordDeserializer(DummyData.getClassSchema());
        DummyData resultDummyData = specificRecordDeserializer.deserialize(serializedBytes);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertEquals(dummyData.toString(), resultDummyData.toString());
    }

    @Test
    public void genericSerdes() throws IOException {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        GenericRecord dummyData = new GenericData.Record(DummyData.getClassSchema());
        dummyData.put("name", "Fred");

        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        AvroRecordSerializer<GenericRecord> genericRecordSerializer = new GenericRecordSerializer<>();
        byte[] serializedBytes = genericRecordSerializer.serialize(dummyData);

        AvroRecordDeserializer<GenericRecord> genericRecordDeserializer = new GenericRecordDeserializer(DummyData.getClassSchema());
        GenericRecord resultDummyData = genericRecordDeserializer.deserialize(serializedBytes);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertEquals(dummyData.toString(), resultDummyData.toString());
    }

    @Test
    public void crossSpecToGenSerdes() throws IOException {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        DummyData dummyData = DummyData.newBuilder().setName("Fred").build();

        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        AvroRecordSerializer<DummyData> specificRecordSerializer = new SpecificRecordSerializer<>();
        byte[] serializedBytes = specificRecordSerializer.serialize(dummyData);

        AvroRecordDeserializer<GenericRecord> genericRecordDeserializer = new GenericRecordDeserializer(DummyData.getClassSchema());
        GenericRecord resultDummyData = genericRecordDeserializer.deserialize(serializedBytes);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertEquals(dummyData.toString(), resultDummyData.toString());
    }

    @Test
    public void crossGenToSpecSerdesPayloadAvro() throws IOException {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        GenericRecord dummyData = new GenericData.Record(DummyData.getClassSchema());
        dummyData.put("name", "Fred");

        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        AvroRecordSerializer<GenericRecord> genericRecordSerializer = new GenericRecordSerializer<>();
        byte[] serializedBytes = genericRecordSerializer.serialize(dummyData);

        AvroRecordDeserializer<DummyData> specificRecordDeserializer = new SpecificRecordDeserializer(DummyData.getClassSchema());
        DummyData resultDummyData = specificRecordDeserializer.deserialize(serializedBytes);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertEquals(dummyData.toString(), resultDummyData.toString());
    }
}