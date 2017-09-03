package com.github.frtu.timeseries;

import java.net.URL;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

/**
 * Check InfluxDB connectivity
 * 
 * @author frtu
 */
@Mojo(name = "check", // configurator = "include-project-dependencies",
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class InfluxDbCheckMojo extends AbstractMojo {
	/**
	 * URL for InfluxDB
	 */
	@Parameter(property = "influxdb.url", defaultValue = "http://localhost:8086")
	private URL influxDbURL;

	@Parameter(property = "influxdb.login", defaultValue = "admin")
	private String login;

	@Parameter(property = "influxdb.password", defaultValue = "admin")
	private String password;

	@Parameter(property = "influxdb.database", defaultValue = "_internal")
	private String database;

	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	private MavenProject mavenProject;

	public void execute() throws MojoExecutionException {
		info("Parameter : influxdb.url={}", influxDbURL);
		info("Parameter : influxdb.login={}", login);
		info("Parameter : influxdb.password={}", password);
		info("Parameter : influxdb.database={}", database);

		InfluxDBManager influxDBManager = new InfluxDBManager(influxDbURL, login, password, database);
		influxDBManager.connect();

		info("Is database exist={}", influxDBManager.isDatabaseExists());

		influxDBManager.close();
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