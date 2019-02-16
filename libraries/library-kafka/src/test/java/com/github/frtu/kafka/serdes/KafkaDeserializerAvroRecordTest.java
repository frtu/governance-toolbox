package com.github.frtu.kafka.serdes;

import org.apache.avro.Schema;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.github.frtu.kafka.serdes.BaseKafkaAvroRecordSerdes.CONFIG_KEY_SCHEMA_CLASSPATH_LOCATION;

public class KafkaDeserializerAvroRecordTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaDeserializerAvroRecordTest.class);

    @Test
    public void getSchemaString() {
        final Map<String, Object> configs = new HashMap<>();
        configs.put(CONFIG_KEY_SCHEMA_CLASSPATH_LOCATION, "classpath:dummy_data.avsc");

        final KafkaDeserializerAvroRecord kafkaDeserializerAvroRecord = new KafkaDeserializerAvroRecord();
        final Schema schema = kafkaDeserializerAvroRecord.getSchema(configs);

        LOGGER.debug(schema.toString());
        Assert.notNull(schema, "Schema must not be null");
    }

    @Test
    public void getSchemaWithFile() {
        final Map<String, Object> configs = new HashMap<>();
        configs.put(CONFIG_KEY_SCHEMA_CLASSPATH_LOCATION, new File("src/test/resources/dummy_data.avsc"));

        final KafkaDeserializerAvroRecord kafkaDeserializerAvroRecord = new KafkaDeserializerAvroRecord();
        final Schema schema = kafkaDeserializerAvroRecord.getSchema(configs);

        LOGGER.debug(schema.toString());
        Assert.notNull(schema, "Schema must not be null");
    }

    @Test
    public void getSchemaWithPath() {
        final Map<String, Object> configs = new HashMap<>();
        configs.put(CONFIG_KEY_SCHEMA_CLASSPATH_LOCATION, Paths.get("src/test/resources/dummy_data.avsc"));

        final KafkaDeserializerAvroRecord kafkaDeserializerAvroRecord = new KafkaDeserializerAvroRecord();
        final Schema schema = kafkaDeserializerAvroRecord.getSchema(configs);

        LOGGER.debug(schema.toString());
        Assert.notNull(schema, "Schema must not be null");
    }

    @Test(expected = IllegalStateException.class)
    public void getSchemaNull() {
        final KafkaDeserializerAvroRecord kafkaDeserializerAvroRecord = new KafkaDeserializerAvroRecord();
        kafkaDeserializerAvroRecord.getSchema(new HashMap<>());
    }

    @Test(expected = IllegalStateException.class)
    public void getSchemaEmptyPath() {
        final Map<String, Object> configs = new HashMap<>();
        configs.put(CONFIG_KEY_SCHEMA_CLASSPATH_LOCATION, "");

        final KafkaDeserializerAvroRecord kafkaDeserializerAvroRecord = new KafkaDeserializerAvroRecord();
        kafkaDeserializerAvroRecord.getSchema(configs);
    }

    @Test(expected = IllegalStateException.class)
    public void getSchemaOnlySpacePath() {
        final Map<String, Object> configs = new HashMap<>();
        configs.put(CONFIG_KEY_SCHEMA_CLASSPATH_LOCATION, "     ");

        final KafkaDeserializerAvroRecord kafkaDeserializerAvroRecord = new KafkaDeserializerAvroRecord();
        kafkaDeserializerAvroRecord.getSchema(configs);
    }

    @Test(expected = IllegalStateException.class)
    public void getSchemaFolderPath() {
        final Map<String, Object> configs = new HashMap<>();
        configs.put(CONFIG_KEY_SCHEMA_CLASSPATH_LOCATION, "src/test/resources/");

        final KafkaDeserializerAvroRecord kafkaDeserializerAvroRecord = new KafkaDeserializerAvroRecord();
        kafkaDeserializerAvroRecord.getSchema(configs);
    }

    @Test(expected = IllegalStateException.class)
    public void getSchemaNonExistingPath() {
        final Map<String, Object> configs = new HashMap<>();
        configs.put(CONFIG_KEY_SCHEMA_CLASSPATH_LOCATION, "non/existing/path");

        final KafkaDeserializerAvroRecord kafkaDeserializerAvroRecord = new KafkaDeserializerAvroRecord();
        kafkaDeserializerAvroRecord.getSchema(configs);
    }

    @Test(expected = IllegalStateException.class)
    public void getSchemaUnsupportedType() {
        final Map<String, Object> configs = new HashMap<>();
        configs.put(CONFIG_KEY_SCHEMA_CLASSPATH_LOCATION, new Object());

        final KafkaDeserializerAvroRecord kafkaDeserializerAvroRecord = new KafkaDeserializerAvroRecord();
        kafkaDeserializerAvroRecord.getSchema(configs);
    }
}