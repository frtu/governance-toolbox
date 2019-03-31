package com.github.frtu.kafka.boot;

import com.github.frtu.kafka.boot.consumer.LoggerConsumer;
import com.github.frtu.kafka.boot.producer.WrapperProducer;
import com.github.frtu.kafka.config.consumer.ConsumerConfiguration;
import com.github.frtu.kafka.config.producer.ProducerConfiguration;
import com.github.frtu.serdes.avro.DummyData;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.stream.IntStream;

@SpringBootApplication
@ComponentScan(basePackageClasses = {ConsumerConfiguration.class, LoggerConsumer.class,
        ProducerConfiguration.class, WrapperProducer.class})
//@PropertySource("classpath:kafka-default.properties")
public class ProduceAndConsumeApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ProduceAndConsumeApplication.class, args);
    }

    @Autowired
    private WrapperProducer<Long, DummyData> producer;

    @Autowired
    private ProducerConfiguration<Long, DummyData> producerConfiguration;

    @Override
    public void run(String... strings) throws Exception {
        DummyData dummyData = DummyData.newBuilder()
                .setName("Fred")
                .build();

        IntStream.range(1, 3).forEach(index -> {
            producer.send(1L * index, dummyData);
        });
        producerConfiguration.close();
    }
}