package com.github.frtu.serdes.avro;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericContainer;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

//import org.apache.kafka.common.serialization.Serializer;
//import org.apache.kafka.common.errors.SerializationException;

/**
 * Avro record serializer.
 * <p>
 * NOTE : Can be used for Kafka Serializer but the current library doesn't pollute the dependency with the fat JAR.
 * </p>
 *
 * @param <T> The specific Avro class it is meant to serialize
 * @author frtu
 */
public abstract class AvroRecordSerializer<T extends GenericContainer> { // implements Serializer<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AvroRecordSerializer.class);

    private boolean isFormatJson = false;

    protected AvroRecordSerializer() {
        this(false);
    }

    protected AvroRecordSerializer(boolean isFormatJson) {
        this.isFormatJson = isFormatJson;
    }

    //  @Override
//  public byte[] serialize(String topic, T record) {
//    try {
//      return getBytes(record);
//    } catch (IOException e) {
//      final String errMsg = String.format("Error when serializing object {} to byte[] due to {}", record, e.getMessage());
//      LOGGER.error(errMsg, e);
//      throw new SerializationException(errMsg, e);
//    }
//  }

    protected abstract DatumWriter<T> buildDatumWriter(Schema schema);

    /**
     * @param record an Avro record
     * @return the according bytes
     * @throws IOException Serialization exception
     */
    public byte[] serialize(T record) throws IOException {
        final Schema schema = record.getSchema();
        LOGGER.debug("Serialize record:{} schema:{}", record, schema);

        DatumWriter<T> writer = buildDatumWriter(schema);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Encoder encoder;
        if (this.isFormatJson) {
            encoder = EncoderFactory.get().jsonEncoder(schema, byteArrayOutputStream);
        } else {
            encoder = EncoderFactory.get().binaryEncoder(byteArrayOutputStream, null);
        }
        writer.write(record, encoder);
        encoder.flush();
        byteArrayOutputStream.close();

        byte[] bytes = byteArrayOutputStream.toByteArray();
        LOGGER.debug("Serialize successfully bytes:{}", bytes);
        return bytes;
    }

    //  @Override
    public void close() {
        // nothing to do
    }
}
