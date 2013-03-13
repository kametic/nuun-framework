/**
 * 
 */
package org.nuunframework.kernel.plugin.request;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.nuunframework.kernel.commons.specification.Specification;


/**
 * @author Epo Jemba
 *
 */
public class ClasspathScanRequestBuilder implements Builder<Collection<ClasspathScanRequest>>
{
    
    private Collection<ClasspathScanRequest> requests;
    
    /**
     * 
     */
    public ClasspathScanRequestBuilder()
    {
        requests = new HashSet<ClasspathScanRequest>();
    }
    
    
    public ClasspathScanRequestBuilder specification(Specification<Class<?>> specification)
    {
        
        requests.add(new ClasspathScanRequest(specification));
        
        return this;
    }

    public ClasspathScanRequestBuilder annotationType(Class<? extends Annotation> annotationTypeRequested)
    {
        
        requests.add(new ClasspathScanRequest(RequestType.ANNOTATION_TYPE, annotationTypeRequested));
        
        return this;
    }

    public ClasspathScanRequestBuilder annotationRegex(String annotationRegex)
    {
        
        requests.add(new ClasspathScanRequest(RequestType.ANNOTATION_REGEX_MATCH, annotationRegex));
        
        return this;
    }

    public ClasspathScanRequestBuilder subtypeOf(Class<?> parentTypeRequested)
    {
        
        requests.add(new ClasspathScanRequest(RequestType.SUBTYPE_OF_BY_CLASS, parentTypeRequested));
        
        return this;
    }
    

    public ClasspathScanRequestBuilder descendentTypeOf(Class<?> parentTypeRequested)
    {

        requests.add(new ClasspathScanRequest(RequestType.SUBTYPE_OF_BY_TYPE_DEEP, parentTypeRequested));
        return this;
    }
    
    public ClasspathScanRequestBuilder subtypeOfRegex(String parentTypeRegex)
    {
        
        requests.add(new ClasspathScanRequest(RequestType.SUBTYPE_OF_BY_REGEX_MATCH, parentTypeRegex));
        
        return this;
    }
    
    public ClasspathScanRequestBuilder typeOfRegex(String typeRegex)
    {
        
        requests.add(new ClasspathScanRequest(RequestType.TYPE_OF_BY_REGEX_MATCH, typeRegex));
        
        return this;
    }

    public ClasspathScanRequestBuilder resourcesRegex(String resourcesRegex)
    {
        
        requests.add(new ClasspathScanRequest(RequestType.RESOURCES_REGEX_MATCH, resourcesRegex));
        
        return this;
    }

    
    @Override
    public Collection<ClasspathScanRequest> build()
    {
        return Collections.unmodifiableCollection(requests);
    }
    
    @Override
    public void reset()
    {
        requests.clear();
    }
    
    
}
