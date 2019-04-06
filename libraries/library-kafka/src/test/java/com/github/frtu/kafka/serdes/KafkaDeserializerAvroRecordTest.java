package com.github.frtu.kafka.serdes;

import com.github.frtu.serdes.avro.DummyData;
import com.github.frtu.serdes.avro.specific.SpecificRecordSerializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericContainer;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class KafkaDeserializerAvroRecordTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaDeserializerAvroRecordTest.class);

    @Test
    public void build() throws IOException {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        DummyData dummyData = DummyData.newBuilder()
                .setName("Fred")
                .build();

        final SpecificRecordSerializer specificRecordSerializer = new SpecificRecordSerializer();
        final byte[] serializedBytes = specificRecordSerializer.serialize(dummyData);

        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        final KafkaDeserializerAvroRecord<GenericContainer> genericContainerKafkaDeserializerAvroRecord = KafkaDeserializerAvroRecord.build("classpath:dummy_data.avsc");
        Assert.assertNotNull("build() method should never return null", genericContainerKafkaDeserializerAvroRecord);

        final GenericContainer resultDummyData = genericContainerKafkaDeserializerAvroRecord.deserialize("KafkaTopicName", serializedBytes);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertEquals(dummyData.toString(), resultDummyData.toString());
    }

    @Test
    public void getSchemaString() {
        final KafkaDeserializerAvroRecord kafkaDeserializerAvroRecord = new KafkaDeserializerAvroRecord();
        final Schema schema = kafkaDeserializerAvroRecord.readSchema("classpath:dummy_data.avsc");

        LOGGER.debug(schema.toString());
        Assert.assertNotNull("Schema must not be null", schema);
    }

    @Test
    public void getSchemaWithFile() {
        final KafkaDeserializerAvroRecord kafkaDeserializerAvroRecord = new KafkaDeserializerAvroRecord();
        final Schema schema = kafkaDeserializerAvroRecord.readSchema(new File("src/test/resources/dummy_data.avsc"));

        LOGGER.debug(schema.toString());
        Assert.assertNotNull("Schema must not be null", schema);
    }

    @Test
    public void getSchemaWithPath() {
        final KafkaDeserializerAvroRecord kafkaDeserializerAvroRecord = new KafkaDeserializerAvroRecord();
        final Schema schema = kafkaDeserializerAvroRecord.readSchema(Paths.get("src/test/resources/dummy_data.avsc"));

        LOGGER.debug(schema.toString());
        Assert.assertNotNull("Schema must not be null", schema);
    }

    @Test(expected = IllegalStateException.class)
    public void getSchemaNull() {
        final KafkaDeserializerAvroRecord kafkaDeserializerAvroRecord = new KafkaDeserializerAvroRecord();
        kafkaDeserializerAvroRecord.readSchema(null);
    }

    @Test(expected = IllegalStateException.class)
    public void getSchemaEmptyPath() {
        final KafkaDeserializerAvroRecord kafkaDeserializerAvroRecord = new KafkaDeserializerAvroRecord();
        kafkaDeserializerAvroRecord.readSchema("");
    }

    @Test(expected = IllegalStateException.class)
    public void getSchemaOnlySpacePath() {
        final KafkaDeserializerAvroRecord kafkaDeserializerAvroRecord = new KafkaDeserializerAvroRecord();
        kafkaDeserializerAvroRecord.readSchema("     ");
    }

    @Test(expected = IllegalStateException.class)
    public void getSchemaFolderPath() {
        final KafkaDeserializerAvroRecord kafkaDeserializerAvroRecord = new KafkaDeserializerAvroRecord();
        kafkaDeserializerAvroRecord.readSchema("src/test/resources/");
    }

    @Test(expected = IllegalStateException.class)
    public void getSchemaNonExistingPath() {
        final KafkaDeserializerAvroRecord kafkaDeserializerAvroRecord = new KafkaDeserializerAvroRecord();
        kafkaDeserializerAvroRecord.readSchema("non/existing/path");
    }

    @Test(expected = IllegalStateException.class)
    public void getSchemaUnsupportedType() {
        final KafkaDeserializerAvroRecord kafkaDeserializerAvroRecord = new KafkaDeserializerAvroRecord();
        kafkaDeserializerAvroRecord.readSchema(new Object());
    }
}