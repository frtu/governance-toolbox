package com.github.frtu.spring.reflect;

import org.springframework.util.ClassUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Frédéric TU
 * @see <a href="https://github.com/frtu/SimpleToolbox/blob/master/SimpleMavenPlugins/src/main/java/com/github/frtu/maven/plugin/utils/ClassloaderUtil.java">Moved from old project SimpleToolbox</a>
 * @since 1.0.2
 */
public class ClassloaderUtil {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ClassloaderUtil.class);

    public static void reloadClassloader(List<String> classpathElements) {
        try {
            Set<URL> urls = new HashSet<URL>();
            for (String element : classpathElements) {
                URL url = new File(element).toURI().toURL();
                urls.add(url);
                logger.debug("Add to url list:{}", urls);
            }

            logger.info("All urls to the Thread.currentThread().setContextClassLoader()");
            // ClassLoader currentContextClassLoader = Thread.currentThread().getContextClassLoader();
            ClassLoader currentContextClassLoader = ClassUtils.getDefaultClassLoader();
            ClassLoader newContextClassLoader = URLClassLoader.newInstance(urls.toArray(new URL[0]), currentContextClassLoader);

            // Thread.currentThread().setContextClassLoader(newContextClassLoader);
            ClassUtils.overrideThreadContextClassLoader(newContextClassLoader);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
