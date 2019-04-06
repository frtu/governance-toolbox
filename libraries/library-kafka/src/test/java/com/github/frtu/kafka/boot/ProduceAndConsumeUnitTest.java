package com.github.frtu.kafka.boot;

import com.github.frtu.kafka.boot.consumer.LoggerConsumer;
import com.github.frtu.kafka.boot.producer.WrapperProducer;
import com.github.frtu.serdes.avro.DummyData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static com.github.frtu.kafka.boot.ProduceAndConsumeUnitTest.KAFKA_TOPIC;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Followed the awesome samples from
 * https://codenotfound.com/spring-kafka-embedded-unit-test-example.html
 * https://codenotfound.com/spring-kafka-consumer-producer-example.html
 * https://memorynotfound.com/spring-kafka-and-spring-boot-configuration-example/
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = {KAFKA_TOPIC})
public class ProduceAndConsumeUnitTest {

    public static final String KAFKA_TOPIC = "testtopic";

    @Autowired
    private WrapperProducer<Long, DummyData> producer;

    @Autowired
    private LoggerConsumer<Long, DummyData> consumer;

    @Value("${kafka.nb.published.message}")
    private Integer nbPublishedMessage;

    @Test
    public void testReceive() throws Exception {
        DummyData dummyData = DummyData.newBuilder()
                .setName("Fred")
                .build();

        IntStream.range(0, nbPublishedMessage).forEach(index -> {
            producer.send(1L * index, dummyData, KAFKA_TOPIC);
        });

        consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
        assertThat(consumer.getLatch().getCount()).isEqualTo(0);
    }
}
