package ${groupId}.spring;

import ${groupId}.*;
import ${groupId}.consumer.LoggerConsumer;
import ${groupId}.producer.WrapperProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

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
@EmbeddedKafka(partitions = 1, topics = {ProduceAndConsumeUnitTest.KAFKA_TOPIC})
public class ProduceAndConsumeUnitTest {

    public static final String KAFKA_TOPIC = "testtopic";

    @Autowired
    private WrapperProducer<Long, ${DatamodelClassName}> producer;

    @Autowired
    private LoggerConsumer<Long, ${DatamodelClassName}> consumer;

    @Value("${kafka.nb.published.message}")
    private Integer nbPublishedMessage;

    @Test
    public void testReceive() throws Exception {
        final Random random = new Random();
        IntStream.range(0, nbPublishedMessage).forEach(index->{
            ${DatamodelClassName} sample = ${DatamodelClassName}.newBuilder()
                    .setId(Integer.toString(index % 4))
                    .setName("fred" + (index % 4))
                    .setValue(random.nextFloat())
                    .setEventTime(System.currentTimeMillis())
                    .build();

            producer.send(1L * index, sample, KAFKA_TOPIC);
        });

        consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
        assertThat(consumer.getLatch().getCount()).isEqualTo(0);
    }
}
