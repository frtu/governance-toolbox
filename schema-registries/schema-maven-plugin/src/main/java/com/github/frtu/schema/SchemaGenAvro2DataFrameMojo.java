package com.github.frtu.schema;

import com.github.frtu.schema.utils.Avro2StructTypeGenerator;
import com.github.frtu.schema.utils.FileUtil;
import org.apache.avro.Schema;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Generate a DataFrame schema from Avro schema.
 *
 * @author frtu
 * @see <a href="http://spark.apache.org/docs/2.3.2/api/java/org/apache/spark/sql/types/StructType.html">DataFrame schema</a>
 * @since 1.0.1
 */
@Mojo(name = "avro2df", // configurator = "include-project-dependencies",
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class SchemaGenAvro2DataFrameMojo extends AbstractAvroFiles2xxxMojo {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaGenAvro2DataFrameMojo.class);

    @Override
    protected void generateFile(String fileBaseName, Schema schema) throws IOException {
        final Avro2StructTypeGenerator avro2StructTypeGenerator = new Avro2StructTypeGenerator();
        final String fileContent = avro2StructTypeGenerator.generateStructType(schema);
        final FileUtil.FileData fileData = new FileUtil.FileData(fileBaseName + ".json", fileContent);
        FileUtil.writeIntoFile(fileData, this.outputDirectory);
    }
}
