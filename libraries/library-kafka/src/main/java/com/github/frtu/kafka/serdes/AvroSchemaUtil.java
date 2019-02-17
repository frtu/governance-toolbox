package com.github.frtu.kafka.serdes;

import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class AvroSchemaUtil extends BaseKafkaAvroRecordSerdes {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvroSchemaUtil.class);

    public Schema readSchema(Path path) throws IOException {
        return readSchema(path.toFile());
    }

    public Schema readSchema(File file) throws IOException {
        return readSchema("file:///" + file.getAbsolutePath());
    }

    public Schema readSchema(String location) throws IOException {
        LOGGER.info("Calling readSchema location={}", location);
        Assert.isTrue(!StringUtils.isEmpty(location.trim()), "Path cannot be empty!!");
        final Resource resource = new DefaultResourceLoader().getResource(location);
        return new Schema.Parser().parse(resource.getInputStream());
    }
}
