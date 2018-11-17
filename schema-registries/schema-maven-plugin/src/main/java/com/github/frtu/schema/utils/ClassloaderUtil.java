package com.github.frtu.schema.utils;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is the class that help to add all projects class into the current Maven plugin classpath.
 *
 * @author frtu
 * @see <a href="https://github.com/StubHub-COE-OpenSource/jaxrs-gensite-maven-plugin/blob/master/src/main/java/com/github/coe/gensite/jaxrs/maven/ClassloaderUtil.java">ClassloaderUtil.java</a>
 * @since 0.3.3
 */
public class ClassloaderUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassloaderUtil.class);

    /**
     * Put all the Project classloader libs into the current maven classloader.
     *
     * @return newly created classloader
     */
    public static ClassLoader initMavenWithCompileClassloader(List<String> classpathElements) {
        try {
            Set<URL> urls = new HashSet<>();
            for (String element : classpathElements) {
                URL url = new File(element).toURI().toURL();
                urls.add(url);
                LOGGER.debug("Add to url list:{}", urls);
            }

            LOGGER.info("All urls to the Thread.currentThread().setContextClassLoader()");
            // ClassLoader currentContextClassLoader = Thread.currentThread().getContextClassLoader();
            ClassLoader currentContextClassLoader = ClassUtils.getDefaultClassLoader();
            ClassLoader newContextClassLoader = URLClassLoader.newInstance(urls.toArray(new URL[0]), currentContextClassLoader);

            // Thread.currentThread().setContextClassLoader(newContextClassLoader);
            ClassUtils.overrideThreadContextClassLoader(newContextClassLoader);
            return newContextClassLoader;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Scan the package packageToScan for all the subtypes of subtypesOf
     *
     * @param packageToScan
     * @param subtypesOf
     * @param <T>
     * @return
     * @see <a href="https://github.com/ronmamo/reflections">Reflections library</a>
     */
    public static <T> Set<Class<? extends T>> scanPackageForSubtypesOf(String packageToScan, Class<T> subtypesOf) {
        Reflections reflections = new Reflections(packageToScan);
        final Set<Class<? extends T>> result = reflections.getSubTypesOf(subtypesOf);
        return result;
    }

    /**
     * Scan the package packageToScan for all the subtypes of subtypesOf
     *
     * @param packageToScan
     * @param subtypesOf
     * @param <T>
     * @return
     */
    public static <T> Set<Class<? extends T>> scanPackageForSubtypesOf(ClassLoader classLoader, String packageToScan, String subtypesOf) {
        final Class<T> parentClass = (Class<T>) ClassUtils.resolveClassName(subtypesOf, classLoader);
        return scanPackageForSubtypesOf(packageToScan, parentClass);
    }
}
