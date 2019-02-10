package com.github.frtu.serdes.avro;

import com.github.frtu.serdes.avro.generic.GenericRecordSerdesFactory;
import com.github.frtu.serdes.avro.specific.SpecificRecordSerdesFactory;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class AvroRecordSerDesTest {

    private static SpecificRecordSerdesFactory<DummyData> specificRecordSerdesFactory = new SpecificRecordSerdesFactory(DummyData.getClassSchema());
    private static GenericRecordSerdesFactory genericRecordSerdesFactory = new GenericRecordSerdesFactory(DummyData.getClassSchema());

    @Test
    public void specificSerdes() throws IOException {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        DummyData dummyData = DummyData.newBuilder().setName("Fred").build();

        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        AvroRecordSerializer<DummyData> specificRecordSerializer = specificRecordSerdesFactory.buildSerializer();
        byte[] serializedBytes = specificRecordSerializer.serialize(dummyData);

        AvroRecordDeserializer<DummyData> specificRecordDeserializer = specificRecordSerdesFactory.buildDeserializer();
        DummyData resultDummyData = specificRecordDeserializer.deserialize(serializedBytes);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertEquals(dummyData.toString(), resultDummyData.toString());
    }

    @Test
    public void specificSerdesJSON() throws IOException {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        DummyData dummyData = DummyData.newBuilder().setName("Fred").build();

        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        AvroRecordSerializer<DummyData> specificRecordSerializer = specificRecordSerdesFactory.buildSerializer(true);
        byte[] serializedBytes = specificRecordSerializer.serialize(dummyData);

        AvroRecordDeserializer<DummyData> specificRecordDeserializer = specificRecordSerdesFactory.buildDeserializer(true);
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
        AvroRecordSerializer<GenericRecord> genericRecordSerializer = genericRecordSerdesFactory.buildSerializer();
        byte[] serializedBytes = genericRecordSerializer.serialize(dummyData);

        AvroRecordDeserializer<GenericRecord> genericRecordDeserializer = genericRecordSerdesFactory.buildDeserializer();
        GenericRecord resultDummyData = genericRecordDeserializer.deserialize(serializedBytes);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertEquals(dummyData.toString(), resultDummyData.toString());
    }

    @Test
    public void genericSerdesJSON() throws IOException {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        GenericRecord dummyData = new GenericData.Record(DummyData.getClassSchema());
        dummyData.put("name", "Fred");

        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        AvroRecordSerializer<GenericRecord> genericRecordSerializer = genericRecordSerdesFactory.buildSerializer(true);
        byte[] serializedBytes = genericRecordSerializer.serialize(dummyData);

        AvroRecordDeserializer<GenericRecord> genericRecordDeserializer = genericRecordSerdesFactory.buildDeserializer(true);
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
        AvroRecordSerializer<DummyData> specificRecordSerializer = specificRecordSerdesFactory.buildSerializer();
        byte[] serializedBytes = specificRecordSerializer.serialize(dummyData);

        AvroRecordDeserializer<GenericRecord> genericRecordDeserializer = genericRecordSerdesFactory.buildDeserializer();
        GenericRecord resultDummyData = genericRecordDeserializer.deserialize(serializedBytes);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertEquals(dummyData.toString(), resultDummyData.toString());
    }

    @Test
    public void crossSpecToGenSerdesJSON() throws IOException {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        DummyData dummyData = DummyData.newBuilder().setName("Fred").build();

        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        AvroRecordSerializer<DummyData> specificRecordSerializer = specificRecordSerdesFactory.buildSerializer(true);
        byte[] serializedBytes = specificRecordSerializer.serialize(dummyData);

        AvroRecordDeserializer<GenericRecord> genericRecordDeserializer = genericRecordSerdesFactory.buildDeserializer(true);
        GenericRecord resultDummyData = genericRecordDeserializer.deserialize(serializedBytes);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertEquals(dummyData.toString(), resultDummyData.toString());
    }

    @Test
    public void crossGenToSpecSerdes() throws IOException {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        GenericRecord dummyData = new GenericData.Record(DummyData.getClassSchema());
        dummyData.put("name", "Fred");

        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        AvroRecordSerializer<GenericRecord> genericRecordSerializer = genericRecordSerdesFactory.buildSerializer();
        byte[] serializedBytes = genericRecordSerializer.serialize(dummyData);

        AvroRecordDeserializer<DummyData> specificRecordDeserializer = specificRecordSerdesFactory.buildDeserializer();
        DummyData resultDummyData = specificRecordDeserializer.deserialize(serializedBytes);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertEquals(dummyData.toString(), resultDummyData.toString());
    }

    @Test
    public void crossGenToSpecSerdesJSON() throws IOException {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        GenericRecord dummyData = new GenericData.Record(DummyData.getClassSchema());
        dummyData.put("name", "Fred");

        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        AvroRecordSerializer<GenericRecord> genericRecordSerializer = genericRecordSerdesFactory.buildSerializer(true);
        byte[] serializedBytes = genericRecordSerializer.serialize(dummyData);

        AvroRecordDeserializer<DummyData> specificRecordDeserializer = specificRecordSerdesFactory.buildDeserializer(true);
        DummyData resultDummyData = specificRecordDeserializer.deserialize(serializedBytes);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertEquals(dummyData.toString(), resultDummyData.toString());
    }
}