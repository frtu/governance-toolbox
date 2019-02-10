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
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

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
 */
public class KafkaAvroRecordDeserializer<T extends GenericContainer> extends BaseKafkaAvroRecordSerdes implements Deserializer<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaAvroRecordDeserializer.class);

    private AvroRecordDeserializer<T> avroRecordDeserializer;

    private Schema schema;

    public KafkaAvroRecordDeserializer() {
        this(null);
    }

    protected KafkaAvroRecordDeserializer(Schema schema) {
        this(true, schema);
    }

    protected KafkaAvroRecordDeserializer(boolean isGenericRecord, Schema schema) {
        this(isGenericRecord, schema, false);
    }

    protected KafkaAvroRecordDeserializer(boolean isGenericRecord, Schema schema, boolean isFormatJson) {
        super(isGenericRecord, isFormatJson);
        this.schema = schema;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        if (this.schema == null) {
            this.schema = getSchema(configs);
        }

        final boolean json = isJson(configs);
        if (isGeneric(configs)) {
            LOGGER.info("Create GenericRecordDeserializer with isJson={} schema={}", schema);
            this.avroRecordDeserializer = new GenericRecordDeserializer(schema, json);
        } else {
            LOGGER.info("Create SpecificRecordDeserializer with isJson={} schema={}", schema);
            this.avroRecordDeserializer = new SpecificRecordDeserializer(schema, json);
        }
    }

    protected Schema readSchema(File file) throws IOException {
        return readSchema("file:///" + file.getAbsolutePath());
    }

    protected Schema readSchema(String location) throws IOException {
        LOGGER.info("Calling readSchema location={}", location);
        Assert.isTrue(!StringUtils.isEmpty(location.trim()), "Path cannot be empty!!");
        final Resource resource = new DefaultResourceLoader().getResource(location);
        return new Schema.Parser().parse(resource.getInputStream());
    }

    protected Schema getSchema(Map<String, ?> configs) {
        final Object configurationValue = configs.get(CONFIG_KEY_SCHEMA_CLASSPATH_LOCATION);
        try {
            if (configurationValue instanceof String) {
                LOGGER.debug("Found config={} string={}", CONFIG_KEY_SCHEMA_CLASSPATH_LOCATION, configurationValue);
                return readSchema((String) configurationValue);
            } else if (configurationValue instanceof File) {
                LOGGER.debug("Found config={} file={}", CONFIG_KEY_SCHEMA_CLASSPATH_LOCATION, configurationValue);
                return readSchema((File) configurationValue);
            } else if (configurationValue instanceof Path) {
                Path path = (Path) configurationValue;
                LOGGER.debug("Found config={} path={}", CONFIG_KEY_SCHEMA_CLASSPATH_LOCATION, path);
                return readSchema(path.toFile());
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
            LOGGER.debug("Calling KafkaAvroRecordDeserializer.deserialize topic={} bytes={}", topic, bytes);
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
