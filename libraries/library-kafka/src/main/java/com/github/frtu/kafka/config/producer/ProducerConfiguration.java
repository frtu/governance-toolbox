package com.github.frtu.kafka.config.producer;

import com.github.frtu.kafka.config.commons.BaseKafkaConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import javax.annotation.PreDestroy;

/**
 * Configuration for Spring Kafka producer.
 *
 * @param <K> Type of the key of the published message
 * @param <V> Type of the value of the published message
 * @author fred
 * @since 0.3.7
 */
@Configuration
public class ProducerConfiguration<K, V> extends BaseKafkaConfiguration<K, V> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerConfiguration.class);

    private DefaultKafkaProducerFactory<K, V> defaultKafkaProducerFactory;

    @Bean
    public ProducerFactory<K, V> producerFactory() {
        this.defaultKafkaProducerFactory = new DefaultKafkaProducerFactory(baseKafkaConfigs());
        return this.defaultKafkaProducerFactory;
    }

    @Bean
    public KafkaTemplate<K, V> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @PreDestroy
    public void close() {
        try {
            this.defaultKafkaProducerFactory.destroy();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}