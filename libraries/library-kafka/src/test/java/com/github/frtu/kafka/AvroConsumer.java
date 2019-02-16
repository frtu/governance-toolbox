package com.github.frtu.kafka;

import com.github.frtu.kafka.serdes.DummyDataKafkaDeserializerAvroRecord;
import com.github.frtu.serdes.avro.DummyData;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;

import java.util.Collections;
import java.util.Properties;
import java.util.stream.IntStream;

public class AvroConsumer {
    private static Consumer<Long, DummyData> createConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "DummyDataConsumer");

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, AvroProducer.BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, DummyDataKafkaDeserializerAvroRecord.class.getName());

//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaDeserializerAvroRecord.class.getName());
//        props.put(BaseKafkaAvroRecordSerdes.CONFIG_KEY_SCHEMA_CLASSPATH_LOCATION, "classpath:dummy_data.avsc");
//        props.put(BaseKafkaAvroRecordSerdes.CONFIG_KEY_GENERIC_AVRO_READER, Boolean.FALSE);
//        props.put(BaseKafkaAvroRecordSerdes.CONFIG_KEY_IS_JSON, Boolean.TRUE);

        return new KafkaConsumer<>(props);
    }

    public static void main(String... args) {
        final Consumer<Long, DummyData> consumer = createConsumer();
        consumer.subscribe(Collections.singletonList(AvroProducer.TOPIC));

        IntStream.range(1, 100).forEach(index -> {
            final ConsumerRecords<Long, DummyData> records = consumer.poll(100);

            if (records.count() == 0) {
                System.out.println("None found");
            } else records.forEach(record -> {
                System.out.printf("%s %d %d %s \n", record.topic(),
                        record.partition(), record.offset(), record.value());
            });
        });
    }
}
