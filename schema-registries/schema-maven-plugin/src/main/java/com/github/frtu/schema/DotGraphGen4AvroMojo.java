package com.github.frtu.schema;

import com.github.frtu.schema.utils.AvroDotGenerator;
import com.github.frtu.schema.utils.FileUtil;
import org.apache.avro.Schema;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Generate a Dot graph from Avro schema.
 *
 * @author frtu
 * @since 0.3.6
 */
@Mojo(name = "avro2dot", // configurator = "include-project-dependencies",
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class DotGraphGen4AvroMojo extends AbstractAvroFiles2xxxMojo {
    private static final Logger LOGGER = LoggerFactory.getLogger(DotGraphGen4AvroMojo.class);

    @Override
    protected void generateFile(String fileBaseName, Schema schema) throws IOException {
        final AvroDotGenerator avroDotGenerator = new AvroDotGenerator(fileBaseName);
        final String fileContent = avroDotGenerator.generateGraph(schema);

        final String filename = fileBaseName + ".dot";
        LOGGER.debug("Generated dot content and writing to {}", filename);
        FileUtil.writeIntoFile(fileContent, filename, this.outputDirectory);
    }
}
