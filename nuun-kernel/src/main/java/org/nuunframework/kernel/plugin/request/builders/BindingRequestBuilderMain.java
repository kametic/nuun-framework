package org.nuunframework.kernel.plugin.request.builders;

import java.lang.annotation.Annotation;

import org.nuunframework.kernel.commons.specification.Specification;

public  interface BindingRequestBuilderMain 
{
    public BindingRequestBuilderOptionsBuildMain specification(Specification<Class<?>> specification);
    public BindingRequestBuilderOptionsBuildMain annotationType(Class<? extends Annotation> annotationTypeRequested);
    public BindingRequestBuilderOptionsBuildMain annotationRegex(String annotationRegex);
    public BindingRequestBuilderOptionsBuildMain subtypeOf(Class<?> parentTypeRequested);
    public BindingRequestBuilderOptionsBuildMain descendentTypeOf(Class<?> ancestorTypeRequested);
    public BindingRequestBuilderOptionsBuildMain subtypeOfRegex(String parentTypeRegex);
    public BindingRequestBuilderOptionsBuildMain metaAnnotationType(Class<? extends Annotation> metaAnnotationTypeRequested);
    public BindingRequestBuilderOptionsBuildMain metaAnnotationRegex(String metaAnnotationRegex);

    
}