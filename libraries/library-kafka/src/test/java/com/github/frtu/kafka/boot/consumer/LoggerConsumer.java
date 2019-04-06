package com.github.frtu.kafka.boot.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.CountDownLatch;

@Service
public class LoggerConsumer<K, V> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerConsumer.class);

    @Value("${kafka.nb.published.message}")
    private Integer nbPublishedMessage;

    private CountDownLatch latch;

    public CountDownLatch getLatch() {
        return latch;
    }

    @PostConstruct
    public void init() {
        latch = new CountDownLatch(nbPublishedMessage);
    }

    @KafkaListener(topics = "${kafka.default.topic}")
    public void listen(@Payload V message) {
        LOGGER.info("received message='{}'", message);
        latch.countDown();
    }
}