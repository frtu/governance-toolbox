package com.github.frtu.kafka.consumer;

import com.github.frtu.kafka.commons.BaseKafkaConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

/**
 * Configuration for Spring Kafka consumer.
 *
 * @param <K> Type of the key of the published message
 * @param <V> Type of the value of the published message
 * @author fred
 * @since 0.3.7
 */
@Configuration
public class ConsumerConfig<K, V> extends BaseKafkaConfiguration<K, V> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerConfig.class);

    @Bean
    public ConsumerFactory<K, V> consumerFactory() {
        return new DefaultKafkaConsumerFactory(baseKafkaConfigs());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<K, V>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<K, V> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
