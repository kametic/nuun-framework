package org.nuunframework.kernel.internal.scanner;

import java.lang.annotation.Annotation;
import java.util.Collection;

import org.nuunframework.kernel.commons.specification.Specification;

public interface ClasspathScanner
{

    Collection<Class<?>> scanClasspathForAnnotation(Class<? extends Annotation> annotationType);

    Collection<Class<?>> scanClasspathForAnnotationRegex(String annotationTypeName);

    Collection<Class<?>> scanClasspathForMetaAnnotationRegex(String annotationTypeName);

    Collection<Class<?>> scanClasspathForSubTypeClass(Class<?> subType);

    Collection<Class<?>> scanClasspathForTypeRegex(String typeRegex);

    Collection<Class<?>> scanClasspathForSubTypeRegex(String typeRegex);

    Collection<String> scanClasspathForResource(String pattern);

    Collection<Class<?>> scanClasspathForSpecification(Specification<Class<?>> specification);

    Collection<Class<?>> scanClasspathForMetaAnnotation(Class<? extends Annotation> annotationType);
}
