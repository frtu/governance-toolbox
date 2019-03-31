package com.github.frtu.kafka.boot.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class LoggerConsumer<K, V> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerConsumer.class);

    @KafkaListener(topics = "${kafka.default.topic}")
    public void listen(@Payload V message) {
        LOGGER.info("received message='{}'", message);
    }
}