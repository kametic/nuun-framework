package org.nuunframework.kernel.scanner;

import java.util.List;

import org.reflections.scanners.AbstractScanner;

class NameAnnotationScanner extends AbstractScanner
{
    
    private final String annotationRegexName;

    public NameAnnotationScanner(String annotationRegexName)
    {
        this.annotationRegexName = annotationRegexName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void scan(Object cls)
    {
        final String className = getMetadataAdapter().getClassName(cls);
        
        for (String annotationType : (List<String>) getMetadataAdapter().getClassAnnotationNames(cls))
        {
            if (  annotationType.matches(annotationRegexName) )
            { 
                getStore().put(annotationType, className);
            }
        }

    }

}
