package com.github.frtu.schemaregistries;

import java.io.File;
import java.net.URL;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import com.github.frtu.schemaregistries.hortonworks.HortonworksSchemaRegistryManager;
import com.github.frtu.simple.scan.DirectoryScanner;
import com.hortonworks.registries.schemaregistry.avro.AvroSchemaProvider;

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

	/**
	 * Description for this particular version (Build ID, artifact, ...).
	 */
	@Parameter(property = "schema.version.description", defaultValue = "${project.groupId}-${project.artifactId}-${project.version}")
	private String schemaVersionDescription;

	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	private MavenProject mavenProject;

	public void execute() throws MojoExecutionException {
		info("Parameter : schemaregistry.url={}", schemaRegistryUrl);
		info("Parameter : schema.type={}", schemaType);
		info("Parameter : schema.path={}", schemaPath);
		info("Parameter : schema.version.description={}", schemaVersionDescription);

		HortonworksSchemaRegistryManager schemaRegistryPublisher = new HortonworksSchemaRegistryManager(
		        schemaRegistryUrl);
		try {
			info("Init connection to Schema Registry");
			schemaRegistryPublisher.initSchemaRegistry();
		} catch (Exception e) {
			throw new MojoExecutionException(
			        format("ATTENTION : Configured schemaregistry.url={} doesn't link to an existing instance. Please check URL or if server is up!",
			                schemaRegistryUrl),
			        e);
		}
		info("=> Server validation successful !");

		final SchemaTypePublisher schemaTypePublisher = schemaRegistryPublisher.getSchemaTypePublisher(schemaType);

		DirectoryScanner directoryScanner = new DirectoryScanner(file -> {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(schemaVersionDescription).append(" for file=").append(file.getAbsolutePath());
			schemaTypePublisher.publishSchema(file, schemaVersionDescription);
		});
		directoryScanner.setFileExtensionToFilter(schemaTypePublisher.getSchemaFileExtensions());

		info("Scanning directory {}", schemaPath);
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