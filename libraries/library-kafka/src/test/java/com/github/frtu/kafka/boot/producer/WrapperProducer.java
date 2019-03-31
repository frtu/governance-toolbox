package com.github.frtu.kafka.boot.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class WrapperProducer<K, V> {
    private static final Logger LOGGER = LoggerFactory.getLogger(WrapperProducer.class);

    @Autowired
    private KafkaTemplate<K, V> kafkaTemplate;

    @Value("${kafka.default.topic}")
    private String defaultTopic;

    public void send(K key, V message) {
        send(key, message, defaultTopic);
    }

    public void send(K key, V message, String topic) {
        LOGGER.info("sending message='{}' to topic='{}'", message, topic);
        kafkaTemplate.send(topic, key, message);
    }
}