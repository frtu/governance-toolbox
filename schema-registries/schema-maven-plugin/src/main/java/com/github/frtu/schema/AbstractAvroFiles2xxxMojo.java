package com.github.frtu.schema;

import com.github.frtu.simple.scan.DirectoryScanner;
import org.apache.avro.Schema;
import org.apache.commons.io.FilenameUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Parent class for all Mojos that scan the avro schema folder read the file content and
 * generate something into outputDirectory.
 *
 * @author frtu
 * @since 1.0.1
 */
public abstract class AbstractAvroFiles2xxxMojo extends AbstractMojo {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAvroFiles2xxxMojo.class);

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
                generateFile(baseName, schema);
            } catch (IOException e) {
                LOGGER.error("Error in loading file from {}. Message={}", absolutePath, e.getMessage(), e);
            }
        });
        final String fileExt = "avsc";
        directoryScanner.setFileExtensionToFilter(fileExt);

        LOGGER.info("Scanning directory {} for file extension {}", schemaPath, fileExt);
        directoryScanner.scanDirectory(schemaPath);
    }

    protected abstract void generateFile(String fileBaseName, Schema schema) throws IOException;
}
