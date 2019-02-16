package com.github.frtu.kafka.serdes;

import com.github.frtu.serdes.avro.AvroRecordSerializer;
import com.github.frtu.serdes.avro.generic.GenericRecordSerializer;
import com.github.frtu.serdes.avro.specific.SpecificRecordSerializer;
import org.apache.avro.generic.GenericContainer;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Base class for a Kafka {@link Serializer} of an Avro record serializer.
 * You CAN to extends this class with the super constructor passing false.
 *
 * @param <T> The generic Avro class it is meant to serialize
 * @author frtu
 * @since 0.3.6
 */
public class KafkaSerializerAvroRecord<T extends GenericContainer> extends BaseKafkaAvroRecordSerdes implements Serializer<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSerializerAvroRecord.class);

    private AvroRecordSerializer<T> avroRecordSerializer;

    public KafkaSerializerAvroRecord() {
        this(GENERIC_AVRO_READER_DEFAULT);
    }

    protected KafkaSerializerAvroRecord(boolean isGenericRecord) {
        this(isGenericRecord, IS_JSON_FORMAT);
    }

    protected KafkaSerializerAvroRecord(boolean isGenericRecord, boolean isFormatJson) {
        super(isGenericRecord, isFormatJson);
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        final boolean json = isJson(configs);
        if (isGeneric(configs)) {
            LOGGER.info("Create GenericRecordSerializer with isJson={}");
            this.avroRecordSerializer = new GenericRecordSerializer(json);
        } else {
            LOGGER.info("Create SpecificRecordDeserializer with isJson={}");
            this.avroRecordSerializer = new SpecificRecordSerializer(json);
        }
    }

    @Override
    public byte[] serialize(String topic, T record) {
        try {
            LOGGER.debug("Calling avroRecordSerializer.serialize topic={} bytes={}", topic, record);
            return avroRecordSerializer.serialize(record);
        } catch (IOException e) {
            final String errMsg = String.format("Error when serializing object {} to byte[] due to {}", record, e.getMessage());
            LOGGER.error(errMsg, e);
            throw new SerializationException(errMsg, e);
        }
    }

    @Override
    public void close() {
        avroRecordSerializer.close();
    }
}
