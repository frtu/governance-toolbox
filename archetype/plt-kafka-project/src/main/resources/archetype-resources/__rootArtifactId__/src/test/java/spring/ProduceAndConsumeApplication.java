package ${groupId}.spring;

import ${groupId}.*;
import ${groupId}.consumer.LoggerConsumer;
import ${groupId}.producer.WrapperProducer;
import com.github.frtu.kafka.config.consumer.ConsumerConfiguration;
import com.github.frtu.kafka.config.producer.ProducerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Move this class into src/main/java if you want to create an application instead of a library.
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = {ConsumerConfiguration.class, LoggerConsumer.class,
        ProducerConfiguration.class, WrapperProducer.class})
public class ProduceAndConsumeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProduceAndConsumeApplication.class, args);
    }
}