package org.nuunframework.kernel.scanner;

import java.util.List;

import org.reflections.scanners.AbstractScanner;

class NameAnnotationScanner extends AbstractScanner
{
    
    private final String annotationName;

    public NameAnnotationScanner(String annotationName)
    {
        this.annotationName = annotationName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void scan(Object cls)
    {
        final String className = getMetadataAdapter().getClassName(cls);

        for (String annotationType : (List<String>) getMetadataAdapter().getClassAnnotationNames(cls))
        {
            if (acceptResult(annotationType) ||   annotationType.endsWith(annotationName)  )
            { 
                getStore().put(annotationType, className);
            }
        }

    }

}
