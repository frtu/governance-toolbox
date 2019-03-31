package com.github.frtu.kafka.config.commons;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * Bootstrap for kafka configuration using property files and override method.
 * <p>
 * Refer to {@link #baseKafkaConfigs()} for overriding sequence.
 *
 * @param <K> Type of the key of the published message
 * @param <V> Type of the value of the published message
 * @author fred
 * @since 0.3.7
 */
@Configuration
public class BaseKafkaConfiguration<K, V> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseKafkaConfiguration.class);

    public static final String KAFKA_PROPERTIES_PATH = "/kafka.properties";
    public static final String KAFKA_DEFAULT_PROPERTIES_PATH = "/kafka-default.properties";

    public static final String KAFKA_CONFIG_KEY_SPRING_KAFKA_BOOTSTRAP_SERVERS = "spring.kafka.bootstrap-servers";

    @Value("${" + KAFKA_CONFIG_KEY_SPRING_KAFKA_BOOTSTRAP_SERVERS + ":}")
    private String bootstrapServers;

    /**
     * Override this method to change how to get Kafka config bootstrap.servers
     *
     * @return Value for bootstrap.servers
     */
    public String getBootstrapServers() {
        return bootstrapServers;
    }

    /**
     * Create a base config using kafka property files
     * (a default one for publisher AND consumer, a specific one for publisher OR consumer).
     * <p>
     * If {@link #getBootstrapServers()} has value, always use it as primary !
     *
     * @return All the properties needed for Kafka
     */
    public Properties baseKafkaConfigs() {
        Properties properties = new Properties();
        // Load properties first
        try {
            properties = loadProperties();
        } catch (IOException e) {
            // No configuration found !
            LOGGER.info("No property file found in the classpath : {} or {}", KAFKA_DEFAULT_PROPERTIES_PATH, KAFKA_PROPERTIES_PATH);
        }

        // Fill with getBootstrapServers()
        if (!StringUtils.isEmpty(getBootstrapServers())) {
            properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, getBootstrapServers());
        }

        // Final check, if BOOTSTRAP_SERVERS_CONFIG is empty, something is wrong !!
        if (StringUtils.isEmpty(properties.getProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG))) {
            final String errMsg = String.format("bootstrapServers field empty ! You MUST to have a property '%s' OR override getBootstrapServers() method !", KAFKA_CONFIG_KEY_SPRING_KAFKA_BOOTSTRAP_SERVERS);
            throw new IllegalStateException(errMsg);
        }
        return properties;
    }

    protected Properties loadProperties() throws IOException {
        Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource(KAFKA_DEFAULT_PROPERTIES_PATH));

        ClassPathResource resource = new ClassPathResource(KAFKA_PROPERTIES_PATH);
        if (resource.exists()) {
            properties.putAll(PropertiesLoaderUtils.loadProperties(resource));
        }
        return properties;
    }

    protected void checkKeyAndCopyIfEmpty(Properties properties, String key, String copyFromKey) {
        if (StringUtils.isEmpty(properties.getProperty(key))) {
            properties.put(key, properties.getProperty(copyFromKey));
        }
    }
}
