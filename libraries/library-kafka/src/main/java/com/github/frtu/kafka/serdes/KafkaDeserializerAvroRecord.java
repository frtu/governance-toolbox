package com.github.frtu.kafka.serdes;

import com.github.frtu.serdes.avro.AvroRecordDeserializer;
import com.github.frtu.serdes.avro.generic.GenericRecordDeserializer;
import com.github.frtu.serdes.avro.specific.SpecificRecordDeserializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericContainer;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

/**
 * Base class for a Kafka {@link Deserializer} of an Avro record deserializer.
 * You MUST configure this class with the right schema OR extends it with the right Avro type.
 *
 * @param <T> The specific Avro class it is meant to deserialize
 * @author frtu
 * @since 0.3.6
 */
public class KafkaDeserializerAvroRecord<T extends GenericContainer> extends BaseKafkaAvroRecordSerdes implements Deserializer<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaDeserializerAvroRecord.class);

    private static AvroSchemaUtil avroSchemaUtil = new AvroSchemaUtil();

    private AvroRecordDeserializer<T> avroRecordDeserializer;
    private Schema schema;

    /**
     * Default constructor to pass to Kafka Consumer.
     * MUST be initialized with {@link #configure(Map, boolean)} before calling {@link #deserialize(String, byte[])}
     */
    public KafkaDeserializerAvroRecord() {
        super();
    }

    /**
     * Parametrized constructor. Schema MUST NOT be null !
     *
     * @param schema Avro schema to deserialize bytes from.
     * @throws IllegalStateException IF schema is erroneous.
     */
    protected KafkaDeserializerAvroRecord(Schema schema) {
        this(true, schema);
    }

    protected KafkaDeserializerAvroRecord(boolean isGenericRecord, Schema schema) {
        this(isGenericRecord, schema, false);
    }

    protected KafkaDeserializerAvroRecord(boolean isGenericRecord, Schema schema, boolean isFormatJson) {
        super(isGenericRecord, isFormatJson);
        Assert.notNull(schema, "Schema MUST NOT be null to be able to deserialize object !");
        this.schema = schema;
        this.avroRecordDeserializer = this.<T>buildAvroRecordDeserializer(this.schema, isFormatJson, isGenericRecord);
    }

    private <T> AvroRecordDeserializer<T> buildAvroRecordDeserializer(Schema schema, boolean json, boolean generic) {
        final AvroRecordDeserializer<T> avroRecordDeserializer;
        if (generic) {
            LOGGER.info("Create GenericRecordDeserializer with isJson={} schema={}", schema);
            avroRecordDeserializer = new GenericRecordDeserializer(schema, json);
        } else {
            LOGGER.info("Create SpecificRecordDeserializer with isJson={} schema={}", schema);
            avroRecordDeserializer = new SpecificRecordDeserializer(schema, json);
        }
        return avroRecordDeserializer;
    }

    public static <T extends GenericContainer> KafkaDeserializerAvroRecord<T> build(String location) throws IOException {
        final Schema schema = avroSchemaUtil.readSchema(location);
        return new KafkaDeserializerAvroRecord(schema);
    }

    /**
     * Lazy initialiation with Kafka config map if calling the default constructor.
     *
     * @param configs configs in key/value pairs
     * @param isKey   whether is for key or value
     */
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        if (this.schema == null) {
            final Object configurationValue = configs.get(CONFIG_KEY_SCHEMA_CLASSPATH_LOCATION);
            this.schema = getSchema(configurationValue);
        }
        if (this.avroRecordDeserializer == null) {
            final boolean isFormatJson = isJson(configs);
            final boolean isGenericRecord = isGeneric(configs);
            this.avroRecordDeserializer = this.<T>buildAvroRecordDeserializer(this.schema, isFormatJson, isGenericRecord);
        }
    }

    protected Schema getSchema(Object configurationValue) {
        try {
            if (configurationValue instanceof String) {
                LOGGER.debug("Found config={} string={}", CONFIG_KEY_SCHEMA_CLASSPATH_LOCATION, configurationValue);
                return avroSchemaUtil.readSchema((String) configurationValue);
            } else if (configurationValue instanceof File) {
                LOGGER.debug("Found config={} file={}", CONFIG_KEY_SCHEMA_CLASSPATH_LOCATION, configurationValue);
                return avroSchemaUtil.readSchema((File) configurationValue);
            } else if (configurationValue instanceof Path) {
                LOGGER.debug("Found config={} path={}", CONFIG_KEY_SCHEMA_CLASSPATH_LOCATION, (Path) configurationValue);
                return avroSchemaUtil.readSchema((Path) configurationValue);
            }
        } catch (IOException | IllegalArgumentException e) {
            throw new IllegalStateException(String.format(
                    "%s must be configured with a valid kafka property! key=%s value=%s",
                    this.getClass(),
                    CONFIG_KEY_SCHEMA_CLASSPATH_LOCATION,
                    configurationValue)
                    , e);
        }
        throw new IllegalStateException(String.format(
                "%s must be configured with a valid kafka property! key=%s value=%s",
                this.getClass(),
                CONFIG_KEY_SCHEMA_CLASSPATH_LOCATION,
                configurationValue)
        );
    }

    @Override
    public T deserialize(String topic, byte[] bytes) {
        try {
            LOGGER.debug("Calling KafkaDeserializerAvroRecord.deserialize topic={} bytes={}", topic, bytes);
            T record = avroRecordDeserializer.deserialize(bytes);
            return record;
        } catch (IOException e) {
            final String errMsg = String.format("Error when deserializing bytes {} due to {}", bytes, e.getMessage());
            LOGGER.error(errMsg, e);
            throw new SerializationException(errMsg, e);
        }
    }

    @Override
    public void close() {
        avroRecordDeserializer.close();
    }
}
