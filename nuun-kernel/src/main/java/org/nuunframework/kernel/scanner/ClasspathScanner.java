package org.nuunframework.kernel.scanner;

import java.lang.annotation.Annotation;
import java.util.Collection;

import org.nuunframework.kernel.commons.specification.Specification;

public interface ClasspathScanner
{

    Collection<Class<?>> scanClasspathForAnnotation(Class<? extends Annotation> annotationType);

    Collection<Class<?>> scanClasspathForAnnotationRegex(String annotationTypeName);

    Collection<Class<?>> scanClasspathForSubTypeClass(Class<?> subType);

    Collection<Class<?>> scanClasspathForTypeRegex(String typeRegex);

    Collection<Class<?>> scanClasspathForSubTypeRegex(String typeRegex);

    Collection<String> scanClasspathForResource(String pattern);

    Collection<Class<?>> scanClasspathForSpecification(Specification<Class<?>> specification);
}
