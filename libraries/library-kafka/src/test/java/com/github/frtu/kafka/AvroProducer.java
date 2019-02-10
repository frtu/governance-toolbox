package com.github.frtu.kafka;

import com.github.frtu.kafka.serdes.BaseKafkaAvroRecordSerdes;
import com.github.frtu.kafka.serdes.KafkaAvroRecordSerializer;
import com.github.frtu.serdes.avro.DummyData;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;

import java.util.Properties;
import java.util.stream.IntStream;

public class AvroProducer {
    public final static String BOOTSTRAP_SERVERS = "localhost:9092";
    public final static String TOPIC = "DummyData-NEW-topic";

    private static Producer<Long, ?> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "DummyDataProducer");

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroRecordSerializer.class.getName());

//        props.put(BaseKafkaAvroRecordSerdes.CONFIG_KEY_GENERIC_AVRO_READER, Boolean.FALSE);
//        props.put(BaseKafkaAvroRecordSerdes.CONFIG_KEY_IS_JSON, Boolean.TRUE);

        props.put("acks", "all");
        props.put("retries", 0);

        return new KafkaProducer<>(props);
    }

    public static void main(String... args) {
        Producer<Long, ?> producer = createProducer();

        DummyData dummyData = DummyData.newBuilder()
                .setName("Fred")
                .build();

//        GenericRecord dummyData = new GenericData.Record(DummyData.getClassSchema());
//        dummyData.put("name", "Fred");

        IntStream.range(1, 3).forEach(index -> {
            producer.send(new ProducerRecord(TOPIC, 1L * index, dummyData));
        });
        producer.flush();
        producer.close();
    }
}
