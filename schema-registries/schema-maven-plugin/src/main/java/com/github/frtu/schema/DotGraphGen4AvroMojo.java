package com.github.frtu.schema;

import com.github.frtu.schema.utils.AvroDotGenerator;
import com.github.frtu.schema.utils.FileUtil;
import com.github.frtu.simple.scan.DirectoryScanner;
import org.apache.avro.Schema;
import org.apache.commons.io.FilenameUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Generate a Dot graph from Avro schema.
 *
 * @author frtu
 * @since 0.3.6
 */
@Mojo(name = "avro2dot", // configurator = "include-project-dependencies",
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class DotGraphGen4AvroMojo extends AbstractMojo {
    private static final Logger LOGGER = LoggerFactory.getLogger(DotGraphGen4AvroMojo.class);

    /**
     * Schemas directory folder
     */
    @Parameter(property = "schema.path", defaultValue = "${project.basedir}/src/main/avro/")
    private File schemaPath;

    /**
     * This is where the output is generated.
     */
    @Parameter(property = "schema.outputDirectory", defaultValue = "${project.build.outputDirectory}")
    protected File outputDirectory;

    public void execute() throws MojoExecutionException {
        LOGGER.info("Parameter IN : schema.path={}", schemaPath);
        LOGGER.info("Parameter OUT : schema.output.directory={}", outputDirectory);

        DirectoryScanner directoryScanner = new DirectoryScanner(file -> {
            String absolutePath = file.getAbsolutePath();
            try {
                final Schema schema = new Schema.Parser().parse(file);
                LOGGER.info("Successfully loaded path={}", absolutePath);

                final String baseName = FilenameUtils.getBaseName(file.getName());
                final AvroDotGenerator avroDotGenerator = new AvroDotGenerator(baseName);
                final String fileContent = avroDotGenerator.generateGraph(schema);

                FileUtil.writeIntoFile(fileContent, baseName + ".dot", this.outputDirectory);
            } catch (IOException e) {
                LOGGER.error("Error in loading file from {}. Message={}", absolutePath, e.getMessage(), e);
            }
        });
        final String fileExt = "avsc";
        directoryScanner.setFileExtensionToFilter(fileExt);

        LOGGER.info("Scanning directory {} for file extension {}", schemaPath, fileExt);
        directoryScanner.scanDirectory(schemaPath);
    }
}
