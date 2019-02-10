package com.github.frtu.kafka.serdes;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class BaseKafkaAvroRecordSerdesTest {
    private static final String CONFIG_KEY = "key";

    @Test
    public void getBooleanConfigTrue() {
        final Map<String, Object> configs = new HashMap<>();
        configs.put(CONFIG_KEY + "1", Boolean.TRUE);
        configs.put(CONFIG_KEY + "2", "true");
        configs.put(CONFIG_KEY + "3", "TRUE");

        final BaseKafkaAvroRecordSerdes baseKafkaAvroRecordSerdes = new BaseKafkaAvroRecordSerdes();
        Assert.assertTrue(baseKafkaAvroRecordSerdes.getBooleanConfig(configs, CONFIG_KEY + "1", false));
        Assert.assertTrue(baseKafkaAvroRecordSerdes.getBooleanConfig(configs, CONFIG_KEY + "2", false));
        Assert.assertTrue(baseKafkaAvroRecordSerdes.getBooleanConfig(configs, CONFIG_KEY + "3", false));
    }

    @Test
    public void getBooleanConfigDefaultValueConfigEmpty() {
        final HashMap<String, Object> configs = new HashMap<>();
        configs.put(CONFIG_KEY + "0", "");

        final BaseKafkaAvroRecordSerdes baseKafkaAvroRecordSerdes = new BaseKafkaAvroRecordSerdes();
        Assert.assertTrue(baseKafkaAvroRecordSerdes.getBooleanConfig(configs, CONFIG_KEY, true));
        Assert.assertFalse(baseKafkaAvroRecordSerdes.getBooleanConfig(configs, CONFIG_KEY, false));
        Assert.assertTrue(baseKafkaAvroRecordSerdes.getBooleanConfig(configs, CONFIG_KEY + "0", true));
        Assert.assertFalse(baseKafkaAvroRecordSerdes.getBooleanConfig(configs, CONFIG_KEY + "0", false));
    }

    @Test
    public void getBooleanConfigDefaultValueConfigError() {
        final HashMap<String, Object> configs = new HashMap<>();
        configs.put(CONFIG_KEY + "_", "abcd");

        final BaseKafkaAvroRecordSerdes baseKafkaAvroRecordSerdes = new BaseKafkaAvroRecordSerdes();
        Assert.assertTrue(baseKafkaAvroRecordSerdes.getBooleanConfig(configs, CONFIG_KEY + "_", true));
        Assert.assertFalse(baseKafkaAvroRecordSerdes.getBooleanConfig(configs, CONFIG_KEY + "_", false));
    }
}