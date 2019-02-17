package com.github.frtu.kafka.serdes;

import java.util.Map;

/**
 * Parent class for a Kafka {@link org.apache.kafka.common.serialization.Deserializer}
 * and {@link org.apache.kafka.common.serialization.Serializer} with Kafka configuration keys.
 *
 * @author frtu
 * @since 0.3.5
 */
public class BaseKafkaAvroRecordSerdes {
    /**
     * Major Kafka configuration key to pass the schema location for deserializer.
     * <p>
     * Value accepted :
     *
     * @see <a href="https://docs.spring.io/spring-framework/docs/4.3.18.RELEASE/spring-framework-reference/html/resources.html#resources-resourceloader">ResourceLoader - Spring framework</a>
     */
    public static final String CONFIG_KEY_SCHEMA_CLASSPATH_LOCATION = "avro.schema.classpath.location";

    /**
     * Kafka configuration key to use JSON for serdes into Kafka. Take more space but more useful for QA.
     * <p>
     * Value accepted : true | false
     */
    public static final String CONFIG_KEY_IS_JSON = "format.isjson";
    /**
     * Minor Kafka configuration key for using Avro generic or specific serdes
     * <p>
     * Value accepted : true | false
     */
    public static final String CONFIG_KEY_GENERIC_AVRO_READER = "avro.serdes.isgeneric";

    public static final boolean GENERIC_AVRO_READER_DEFAULT = true;
    public static final boolean IS_JSON_FORMAT = false;

    private boolean isGenericRecord = GENERIC_AVRO_READER_DEFAULT;
    private boolean isFormatJson = IS_JSON_FORMAT;

    protected BaseKafkaAvroRecordSerdes() {
        this(GENERIC_AVRO_READER_DEFAULT, IS_JSON_FORMAT);
    }

    protected BaseKafkaAvroRecordSerdes(boolean isGenericRecord, boolean isFormatJson) {
        this.isGenericRecord = isGenericRecord;
        this.isFormatJson = isFormatJson;
    }

    protected boolean isJson(Map<String, ?> configs) {
        return getBooleanConfig(configs, CONFIG_KEY_IS_JSON, this.isFormatJson);
    }

    protected boolean isGeneric(Map<String, ?> configs) {
        return getBooleanConfig(configs, CONFIG_KEY_GENERIC_AVRO_READER, this.isGenericRecord);
    }

    protected boolean getBooleanConfig(Map<String, ?> configs, String configKey, boolean configurationValueDefault) {
        final Object configurationValue = configs.get(configKey);
        return (configurationValue != null && Boolean.valueOf(configurationValue.toString())) || configurationValueDefault;
    }
}
