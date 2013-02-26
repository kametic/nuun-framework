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
public class BindingRequestBuilder implements Builder<Collection<BindingRequest>>
{
    
    private Collection<BindingRequest> requests;
    
    /**
     * 
     */
    public BindingRequestBuilder()
    {
        requests = new HashSet<BindingRequest>();
    }
    
    
    
    // VIA SPECIFICATION
    public BindingRequestBuilder specification(Specification<Class<?>> specification)
    {
        
        requests.add(new BindingRequest(RequestType.VIA_SPECIFICATION, null,specification));
        
        return this;
    }

    public BindingRequestBuilder specification(Specification<Class<?>> specification, Object scope)
    {
        requests.add(new BindingRequest(RequestType.VIA_SPECIFICATION, scope,specification));
        return this;
    }
    
    // ANNOTATION TYPE
    public BindingRequestBuilder annotationType(Class<? extends Annotation> annotationTypeRequested)
    {
        
        requests.add(new BindingRequest(RequestType.ANNOTATION_TYPE, annotationTypeRequested));
        
        return this;
    }
    
    public BindingRequestBuilder annotationType(Class<? extends Annotation> annotationTypeRequested, Object scope)
    {
        
        requests.add(new BindingRequest(RequestType.ANNOTATION_TYPE, annotationTypeRequested,scope,null));
        
        return this;
    }
    
    // ANNOTATION_REGEX_MATCH
    public BindingRequestBuilder annotationRegex(String annotationRegex)
    {
        
        requests.add(new BindingRequest(RequestType.ANNOTATION_REGEX_MATCH, annotationRegex));
        
        return this;
    }
    public BindingRequestBuilder annotationRegex(String annotationRegex, Object scope)
    {
        
        requests.add(new BindingRequest(RequestType.ANNOTATION_REGEX_MATCH, annotationRegex,scope,null));
        
        return this;
    }

    // SUBTYPE_OF_BY_CLASS
    
    public BindingRequestBuilder subtypeOf(Class<?> parentTypeRequested)
    {
        
        requests.add(new BindingRequest(RequestType.SUBTYPE_OF_BY_CLASS, parentTypeRequested));
        
        return this;
    }
    public BindingRequestBuilder subtypeOf(Class<?> parentTypeRequested, Object scope)
    {
        
        requests.add(new BindingRequest(RequestType.SUBTYPE_OF_BY_CLASS, parentTypeRequested,scope,null));
        
        return this;
    }
    
    // SUBTYPE_OF_BY_REGEX_MATCH
    
    public BindingRequestBuilder subtypeOfRegex(String parentTypeRegex)
    {
        
        requests.add(new BindingRequest(RequestType.SUBTYPE_OF_BY_REGEX_MATCH, parentTypeRegex));
        
        return this;
    }

    public BindingRequestBuilder subtypeOfRegex(String parentTypeRegex,Object scope)
    {
        
        requests.add(new BindingRequest(RequestType.SUBTYPE_OF_BY_REGEX_MATCH, parentTypeRegex,scope,null));
        
        return this;
    }

    
    @Override
    public Collection<BindingRequest> build()
    {
        return Collections.unmodifiableCollection(requests);
    }
    
    @Override
    public void reset()
    {
        requests.clear();
    }
    
    
}
