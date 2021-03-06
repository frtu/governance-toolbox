package com.github.frtu.spring.annotation;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * Scan a {@link Class} for {@link Annotation} and return result in an inner {@link Multimap} that you can cache.
 *
 * @param <MethodAnno> Annotation class used on method
 * @param <ParamAnno>  Annotation class used on method parameter
 * @author Frédéric TU
 * @see <a href="https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/core/annotation/AnnotationUtils.html">Rely on Spring {@link AnnotationUtils}</a>
 * @see <a href="https://github.com/frtu/log-platform/blob/v0.9.4/logger-libraries/logger-core/src/main/java/com/github/frtu/utils/AnnotationMethodScanner.java">Moved from log-platform project</a>
 * @since 1.0.1
 */
public class AnnotationMethodScanner<MethodAnno extends Class<? extends Annotation>, ParamAnno extends Class<? extends Annotation>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationMethodScanner.class);

    private Class<? extends Annotation> methodAnnotationClass;
    private Class<? extends Annotation> paramAnnotationClass;

    /**
     * Create an {@link AnnotationMethodScanner} by capturing {@link Annotation} class annotated on
     * {@link Method} and on {@link Parameter}.
     *
     * @param methodAnnotationClass Annotation class at method level.
     * @param paramAnnotationClass  Annotation class at parameter level.
     * @param <M>                   Annotation class used on method
     * @param <P>                   Annotation class used on method parameter
     * @return Instance of {@link AnnotationMethodScanner}
     */
    public static <M extends Class<? extends Annotation>, P extends Class<? extends Annotation>>
    AnnotationMethodScanner<M, P> of(M methodAnnotationClass, P paramAnnotationClass) {
        LOGGER.trace("Create an AnnotationMethodScanner for MethodAnno='{}' and ParamAnno='{}'", methodAnnotationClass, paramAnnotationClass);
        return new AnnotationMethodScanner<>(methodAnnotationClass, paramAnnotationClass);
    }

    /**
     * Use {@link #of(Class, Class)} instead.
     *
     * @param methodAnnotationClass Annotation class used on method
     * @param paramAnnotationClass  Annotation class used on method parameter
     */
    private AnnotationMethodScanner(Class<? extends Annotation> methodAnnotationClass, Class<? extends Annotation> paramAnnotationClass) {
        this.methodAnnotationClass = methodAnnotationClass;
        this.paramAnnotationClass = paramAnnotationClass;
    }

    /**
     * Scan a {@link Method} for annotation class 'MethodAnno' and extract nested annotations
     * and for annotations on {@link Parameter} of annotation class 'ParamAnno'.
     *
     * @param method Instance of the method to scan
     * @return An object containing all the found annotations on this method.
     */
    public AnnotationMethodScan scan(Method method) {
        return scan(method, methodAnnotationClass, paramAnnotationClass);
    }

    public static AnnotationMethodScan scan(Method method, Class<? extends Annotation> methodAnnotationClass, Class<? extends Annotation> paramAnnotationClass) {
        return scan(method, method.getAnnotation(methodAnnotationClass), paramAnnotationClass);
    }

    public static AnnotationMethodScan scan(Method method, Annotation methodAnnotation, Class<? extends Annotation> paramAnnotationClass) {
        LOGGER.trace("Scan method for MethodAnno='{}' and ParamAnno='{}'", method, methodAnnotation, paramAnnotationClass);
        if (methodAnnotation == null) {
            return AnnotationMethodScan.EMPTY;
        }
        Map<String, Object> annotationAttributes = getAnnotationAttributes(methodAnnotation);
        Annotation[] annotationFromParams = getAnnotationFromParams(method, paramAnnotationClass);
        return new AnnotationMethodScan(method, annotationAttributes, annotationFromParams);
    }

    /**
     * Scan all {@link Method} of this {@link Class} for annotation class 'MethodAnno' extract nested annotations
     * and annotations on {@link Parameter} of annotation class 'ParamAnno'.
     *
     * @param <M>         Annotation class used on method
     * @param <P>         Annotation class used on method parameter
     * @param targetClass Class to scan for annotation 'MethodAnno'
     * @return An object containing all the found annotation on this method.
     */
    public <M extends Class<? extends Annotation>, P extends Class<? extends Annotation>> Multimap<String, AnnotationMethodScan<M, P>> scan(Class<?> targetClass) {
        final Method[] declaredMethods = targetClass.getDeclaredMethods();

        Multimap<String, AnnotationMethodScan<M, P>> methodNameMultimap = ArrayListMultimap.create();
        for (Method method : declaredMethods) {
            final AnnotationMethodScan annotationMethodScan = scan(method);
            // Only put method annotated with 'MethodAnno'
            if (!AnnotationMethodScan.EMPTY.equals(annotationMethodScan)) {
                methodNameMultimap.put(method.getName(), annotationMethodScan);
            }
        }
        return methodNameMultimap;
    }

    static Map<String, Object> getAnnotationAttributes(Annotation annotation) {
        //                boolean isEmptyAnnotationAttributes = true;
//                for (Object object : annotationAttributes.values()) {
//                    if (!isArray(object)) {
//                        isEmptyAnnotationAttributes = false;
//                    } else {
//                        ArrayUtils.
//                        //
//                    }
//                }
        return AnnotationUtils.getAnnotationAttributes(annotation);
    }

    /**
     * @param method               Instance of the method to scan
     * @param paramAnnotationClass Annotation class that marks a parameter
     * @return Array of annotation parameter if paramAnnotationClass used. Else return empty array if nothing found.
     */
    static Annotation[] getAnnotationFromParams(Method method, Class<? extends Annotation> paramAnnotationClass) {
        final Parameter[] parameters = method.getParameters();
        Annotation[] paramAnnotation = new Annotation[parameters.length];
        boolean isEmptyParamAnnotation = true;

        for (int i = 0; i < parameters.length; i++) {
            final Annotation[] annotations = parameters[i].getDeclaredAnnotationsByType(paramAnnotationClass);
            if (annotations.length > 0) {
                isEmptyParamAnnotation = false;
                paramAnnotation[i] = annotations[0];
            } else {
                paramAnnotation[i] = null;
            }
        }
        if (isEmptyParamAnnotation) {
            paramAnnotation = new Annotation[0];
        }
        return paramAnnotation;
    }
}
