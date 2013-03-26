package org.nuunframework.kernel.internal.scanner;

import java.lang.annotation.Annotation;

import org.nuunframework.kernel.commons.AssertUtils;
import org.reflections.scanners.AbstractScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetaAnnotationScanner extends AbstractScanner
{

    Logger logger = LoggerFactory.getLogger(MetaAnnotationScanner.class);
            
    private final Class<? extends Annotation> annotationType;

    private final String metaAnnotationRegex;

    public MetaAnnotationScanner(Class<? extends Annotation> annotationType)
    {
        this.annotationType = annotationType;
        this.metaAnnotationRegex = null;
    }
    
    public MetaAnnotationScanner(String  metaAnnotationRegex)
    {
        this.metaAnnotationRegex = metaAnnotationRegex;
        this.annotationType = null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void scan(Object cls)
    {
        final String className = getMetadataAdapter().getClassName(cls);
        try
        {
            Class<?> klass = Class.forName(className);
            
            if ( annotationType != null &&  AssertUtils.hasAnnotationDeep(klass, annotationType) && ! klass.isAnnotation() )
            {
                getStore().put(annotationType.getName(), className);
            }
            
            if ( metaAnnotationRegex != null &&  AssertUtils.hasAnnotationDeepRegex(klass, metaAnnotationRegex) && ! klass.isAnnotation() )
            {
                getStore().put(metaAnnotationRegex, className);
            }
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

    }

}
