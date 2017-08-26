package com.github.frtu.schemaregistries;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import com.github.frtu.simple.scan.DirectoryScanner;
import com.github.frtu.simple.scan.FileScannerObserver;
import com.hortonworks.registries.schemaregistry.avro.AvroSchemaProvider;
import com.hortonworks.registries.schemaregistry.errors.IncompatibleSchemaException;
import com.hortonworks.registries.schemaregistry.errors.InvalidSchemaException;
import com.hortonworks.registries.schemaregistry.errors.SchemaNotFoundException;

/**
 * Publish project Avro schema into Hortonworks Schema Registry
 * 
 * @author frtu
 */
@Mojo(name = "publish", // configurator = "include-project-dependencies",
requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class SchemaRegistryPublisherMojo extends AbstractMojo {
	/**
	 * URL or Schema Registry
	 */
	@Parameter(property = "schemaregistry.url", defaultValue = "http://localhost:9090")
	private URL schemaRegistryUrl;

	/**
	 * Schema type
	 */
	@Parameter(property = "schema.type", defaultValue = AvroSchemaProvider.TYPE)
	private String schemaType;
	/**
	 * Schemas directory folder
	 */
	@Parameter(property = "schema.path", defaultValue = "${project.basedir}/src/main/avro")
	private File schemaPath;

	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	private MavenProject mavenProject;

	public void execute() throws MojoExecutionException {
		info("Parameter : schemaregistry.url={}", schemaRegistryUrl);
		info("Parameter : schema.type={}", schemaType);
		info("Parameter : schema.path={}", schemaPath);

		SchemaRegistryPublisher schemaRegistryPublisher = new SchemaRegistryPublisher(schemaRegistryUrl);
		try {
			info("Init connection to Schema Registry");
			schemaRegistryPublisher.initSchemaRegistry();
		} catch (IOException e) {
			throw new MojoExecutionException(
					format("ATTENTION : Configured schemaregistry.url={} doesn't link to an existing instance. Please check URL or if server is up!",
							schemaRegistryUrl),
					e);
		}
		info("=> Server validation successful !");
		
		FileScannerObserver fileScanner = new FileScannerObserver() {
			@Override
			public void scanFile(File file) {
				try {
					schemaRegistryPublisher.registerSchema(file);
				} catch (IOException | InvalidSchemaException | IncompatibleSchemaException | SchemaNotFoundException e) {
					throw new IllegalArgumentException(
							format("Failed to publish schema file={}",
									file.getAbsolutePath()),
							e);
				}
			}
		};
		DirectoryScanner directoryScanner = new DirectoryScanner(fileScanner);
		if (AvroSchemaProvider.TYPE.equalsIgnoreCase(schemaType)) {
			directoryScanner.setFileExtensionToFilter("avsc");
		}
		directoryScanner.scanDirectory(schemaPath);
	}

	private void debug(String template, Object... argv) {
		getLog().debug(format(template, argv));
	}

	private void info(String template, Object... argv) {
		getLog().info(format(template, argv));
	}

	private void warn(String template, Object... argv) {
		getLog().warn(format(template, argv));
	}

	private void error(String template, Object... argv) {
		getLog().error(format(template, argv));
	}

	private void error(Throwable e, String template, Object... argv) {
		getLog().error(format(template, argv), e);
	}

	private String format(String template, Object... argv) {
		String strTemplate = template.replace("{}", "%s");
		return String.format(strTemplate, argv);
	}
}