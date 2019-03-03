package com.github.frtu.schema;

import com.github.frtu.schema.utils.FileUtil;
import com.github.frtu.schema.utils.SchemaUtil;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Set;

/**
 * Generate Avro schema from all the POJOs that subtype a classname pass in parameter.
 *
 * @author frtu
 * @since 0.3.3
 */
@Mojo(name = "pojo2avro", // configurator = "include-project-dependencies",
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class SchemaGenPojo2AvroMojo extends AbstractPojo2xxxMojo {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaGenPojo2AvroMojo.class);

    @Override
    protected void execute(Set<Class<?>> classSet, File outputDirectory) throws MojoExecutionException {
        for (Class<?> classInstance : classSet) {
            final String canonicalName = classInstance.getCanonicalName();
            try {
                String fileContent = SchemaUtil.genAvroSchemaStringFrom(classInstance);
                FileUtil.writeIntoFile(fileContent, canonicalName + ".avsc", this.outputDirectory);
            } catch (Exception e) {
                LOGGER.error("Error on parsing & writing class {}", canonicalName, e);
            }
        }
    }
}
