package ${groupId};

import com.github.frtu.kafka.serdes.BaseKafkaAvroRecordSerdes;
import com.github.frtu.kafka.serdes.KafkaDeserializerAvroRecord;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import java.util.Collections;
import java.util.Properties;
import java.util.stream.IntStream;

public class AvroConsumer {
    private static Consumer<Long, ${DatamodelClassName}> createConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "${DatamodelClassName}AvroConsumer");

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, AvroProducer.BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());

        //------------------------------------------
        // Confluent Schema registry serdes
        //------------------------------------------
        // Schema registry location. Usually Schema Registry on 8081
//        props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        // Use Specific Record or else you get Avro GenericRecord.
//        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");

        //------------------------------------------
        // Custom local serdes
        //------------------------------------------
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaDeserializerAvroRecord.class.getName());
        props.put(BaseKafkaAvroRecordSerdes.CONFIG_KEY_SCHEMA_CLASSPATH_LOCATION, "classpath:" + avroSchemaFileBaseName + ".avsc");
        props.put(BaseKafkaAvroRecordSerdes.CONFIG_KEY_GENERIC_AVRO_READER, Boolean.FALSE);
        props.put(BaseKafkaAvroRecordSerdes.CONFIG_KEY_IS_JSON, Boolean.TRUE);

        return new KafkaConsumer<>(props);
    }

    public static void main(String... args) {
        final Consumer<Long, ${DatamodelClassName}> consumer = createConsumer();
        consumer.subscribe(Collections.singletonList(AvroProducer.TOPIC));
        IntStream.range(1, 100).forEach(index -> {
            final ConsumerRecords<Long, ${DatamodelClassName}> records =
                    consumer.poll(100);
            if (records.count() == 0) {
                System.out.println("None found");
            } else records.forEach(record -> {
                System.out.printf("%s %d %d %s \n", record.topic(),
                        record.partition(), record.offset(), record.value());
            });
        });
    }
}
