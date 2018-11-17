package com.github.frtu.schema;

import com.github.frtu.schema.utils.ClassloaderUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Parent class for all Mojos that scan the current maven project for all classes matching subtypesOf.
 * Then call child method {@link #execute(Set, File)}.
 *
 * @author frtu
 * @since 0.3.3
 */
public abstract class AbstractPojo2xxxMojo extends AbstractMojo {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaGenPojo2AvroMojo.class);

    /**
     * Define a package to scan. If not define, try to match it with the Maven project GroupId.
     */
    @Parameter(property = "schema.includePackage", defaultValue = "${project.groupId}")
    protected String includePackage;

    /**
     * All the classes which subtype of this full canonical name of an Interface or parent Class.
     */
    @Parameter(property = "schema.subtypesOf")
    protected String subtypesOf;

    /**
     * This is where the output is generated.
     */
    @Parameter(property = "schema.outputDirectory", defaultValue = "${basedir}/target/generated-sources/")
    protected File outputDirectory;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    protected MavenProject mavenProject;

    public void execute() throws MojoExecutionException {
        LOGGER.info("Parameter IN : schema.path={}", includePackage);
        LOGGER.info("Parameter IN : schema.subtypesOf={}", subtypesOf);
        LOGGER.info("Parameter OUT : schema.output.directory={}", outputDirectory);
        Assert.hasText(subtypesOf, "Property 'schema.subtypesOf' MUST be defined with the parent class or interface, to apply the plugin to.");

        // Allow to add the Compile classpath into the current classloader since include-project-dependencies didn't work.
        final ClassLoader fullyInitClassloader = initClassloader();

        // Scan all subtypes classes that match the super class 'subtypesOf'
        final Set<Class<?>> classSet = ClassloaderUtil.scanPackageForSubtypesOf(fullyInitClassloader, includePackage, subtypesOf);
        genScanDescriptor(classSet);

        execute(classSet, outputDirectory);
    }

    private void genScanDescriptor(Set<Class<?>> classSet) throws MojoExecutionException {
        // Descriptor file header
        final StringBuilder descriptorBuilder = new StringBuilder();
        descriptorBuilder.append(String
                .format("Schema generate in project %s:%s:%s\n Parent class = {}", mavenProject.getGroupId(), mavenProject.getArtifactId(),
                        mavenProject.getVersion(), subtypesOf));

        for (Class<?> classInstance : classSet) {
            LOGGER.info("Found class '{}' matching parent '{}'", classInstance.getCanonicalName(), subtypesOf);
            descriptorBuilder.append(" - ").append(classInstance.getCanonicalName()).append('\n');
        }
        writeDescriptor(descriptorBuilder.toString(), outputDirectory);
    }

    protected abstract void execute(Set<Class<?>> classSet, File outputDirectory) throws MojoExecutionException;

    /**
     * Allow to add the Compile classpath into the current classloader since include-project-dependencies didn't work.
     */
    private ClassLoader initClassloader() {
        List<String> classpathElements;
        try {
            classpathElements = mavenProject.getCompileClasspathElements();
        } catch (DependencyResolutionRequiredException e) {
            throw new RuntimeException(e);
        }
        return ClassloaderUtil.initMavenWithCompileClassloader(classpathElements);
    }

    /**
     * Writes a doc.html file with plugin output report.
     */
    private static void writeDescriptor(String descriptor, File directory) throws MojoExecutionException {
        LOGGER.info("Generate file into {}", directory.getAbsolutePath());
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File touch = new File(directory, "doc.html");
        try {
            IOUtils.write(descriptor, new FileOutputStream(touch), Charset.forName("UTF-8"));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new MojoExecutionException(e.getMessage());
        }
    }
}
