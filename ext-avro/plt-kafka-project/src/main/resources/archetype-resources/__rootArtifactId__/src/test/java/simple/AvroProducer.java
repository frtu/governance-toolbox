package ${groupId}.simple;

import com.github.frtu.kafka.serdes.BaseKafkaAvroRecordSerdes;
import com.github.frtu.kafka.serdes.KafkaSerializerAvroRecord;

import ${groupId}.*;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import java.util.Properties;
import java.util.stream.IntStream;
import java.util.Random;

public class AvroProducer {
    public final static String BOOTSTRAP_SERVERS = "localhost:9092";
    public final static String TOPIC = "${DatamodelClassName}-topic";

    private static Producer<Long, ${DatamodelClassName}> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "${DatamodelClassName}AvroProducer");

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());

        //------------------------------------------
        // Confluent Schema registry serdes
        //------------------------------------------
        // Schema registry location. Usually Schema Registry on 8081
//        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());

        //------------------------------------------
        // Custom local serdes
        //------------------------------------------
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaSerializerAvroRecord.class.getName());
        props.put(BaseKafkaAvroRecordSerdes.CONFIG_KEY_IS_JSON, Boolean.TRUE);

        props.put("acks", "all");
        props.put("retries", 0);

        return new KafkaProducer<>(props);
    }

    public static void main(String... args) {
        Producer<Long, ${DatamodelClassName}> producer = createProducer();

        final Random random = new Random();
        IntStream.range(1, 20).forEach(index->{
            ${DatamodelClassName} sample = ${DatamodelClassName}.newBuilder()
                    .setId(Integer.toString(index % 4))
                    .setName("fred" + (index % 4))
                    .setValue(random.nextFloat())
                    .setEventTime(System.currentTimeMillis())
                    .build();

            producer.send(new ProducerRecord<>(TOPIC, 1L * index, sample));
        });
        producer.flush();
        producer.close();
    }
}
