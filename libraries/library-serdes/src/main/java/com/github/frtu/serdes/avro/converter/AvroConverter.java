package com.github.frtu.serdes.avro.converter;

import com.github.frtu.serdes.avro.AvroRecordDeserializer;
import com.github.frtu.serdes.avro.generic.GenericRecordDeserializer;
import com.github.frtu.serdes.avro.generic.GenericRecordSerializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;

import java.io.IOException;
import java.nio.charset.Charset;

public class AvroConverter<T extends GenericRecord> extends GenericRecordSerializer<T> {

    public static final String CHARSET_NAME = "UTF-8";

    private AvroRecordDeserializer<T> avroRecordDeserializer;

    public AvroConverter(Schema schema) {
        this(new GenericRecordDeserializer<>(schema));
    }

    public AvroConverter(GenericRecordDeserializer<T> avroRecordDeserializer) {
        super(true);
        this.avroRecordDeserializer = avroRecordDeserializer;
    }

    /**
     * Convert the specified T object into JSON.
     *
     * @param bytes
     * @return JSON String corresponding to the avro object.
     * @throws IOException
     */
    public String convertBytesToJson(byte[] bytes) throws IOException {
        final T avroObject = avroRecordDeserializer.deserialize(bytes, false);
        final byte[] serializeJson = super.serialize(avroObject, true);
        return new String(serializeJson, Charset.forName(CHARSET_NAME));
    }

    /**
     * Convert the specified T object into JSON.
     *
     * @param jsonString
     * @return JSON String corresponding to the avro object.
     * @throws IOException
     */
    public byte[] convertJsonToBytes(String jsonString) throws IOException {
        final T avroObject = avroRecordDeserializer.deserialize(jsonString.getBytes(CHARSET_NAME), true);
        final byte[] serializeAvro = super.serialize(avroObject, false);
        return serializeAvro;
    }
}
