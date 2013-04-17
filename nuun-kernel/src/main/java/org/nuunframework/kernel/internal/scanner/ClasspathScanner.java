package org.nuunframework.kernel.internal.scanner;

import java.lang.annotation.Annotation;
import java.util.Collection;

import org.nuunframework.kernel.commons.specification.Specification;

public interface ClasspathScanner
{

    void scanClasspathForAnnotation(Class<? extends Annotation> annotationType , Callback callback);

    void scanClasspathForAnnotationRegex(String annotationTypeName, Callback callback);

    void scanClasspathForMetaAnnotationRegex(String annotationTypeName, Callback callback);

    void scanClasspathForSubTypeClass(Class<?> subType, Callback callback);

    void scanClasspathForTypeRegex(String typeRegex, Callback callback);

    void scanClasspathForSubTypeRegex(String typeRegex, Callback callback);

    void scanClasspathForResource(String pattern, CallbackResources callback);

    void scanClasspathForSpecification(Specification<Class<?>> specification, Callback callback);

    void scanClasspathForMetaAnnotation(Class<? extends Annotation> annotationType, Callback callback);
    
    void doClasspathScan ();
    
    
    public static interface Callback 
    {
        public void callback(Collection<Class<?>> scanResult);
    }

    public static interface CallbackResources
    {
        public void callback(Collection<String> scanResult);
    }
    
}
