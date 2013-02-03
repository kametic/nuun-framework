/**
 * 
 */
package org.nuunframework.kernel.plugin.request;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;


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
    
    
    public BindingRequestBuilder annotationType(Class<? extends Annotation> annotationTypeRequested)
    {
        
        requests.add(new BindingRequest(RequestType.ANNOTATION_TYPE, annotationTypeRequested));
        
        return this;
    }

    public BindingRequestBuilder annotationType(Class<? extends Annotation> annotationTypeRequested, Object scope)
    {
        
        requests.add(new BindingRequest(RequestType.ANNOTATION_TYPE, annotationTypeRequested,scope));
        
        return this;
    }

    public BindingRequestBuilder annotationRegex(String annotationRegex)
    {
        
        requests.add(new BindingRequest(RequestType.ANNOTATION_REGEX_MATCH, annotationRegex));
        
        return this;
    }
    public BindingRequestBuilder annotationRegex(String annotationRegex, Object scope)
    {
        
        requests.add(new BindingRequest(RequestType.ANNOTATION_REGEX_MATCH, annotationRegex,scope));
        
        return this;
    }

    public BindingRequestBuilder subtypeOf(Class<?> parentTypeRequested)
    {
        
        requests.add(new BindingRequest(RequestType.SUBTYPE_OF_BY_CLASS, parentTypeRequested));
        
        return this;
    }
    public BindingRequestBuilder subtypeOf(Class<?> parentTypeRequested, Object scope)
    {
        
        requests.add(new BindingRequest(RequestType.SUBTYPE_OF_BY_CLASS, parentTypeRequested,scope));
        
        return this;
    }
    
    public BindingRequestBuilder subtypeOfRegex(String parentTypeRegex)
    {
        
        requests.add(new BindingRequest(RequestType.SUBTYPE_OF_BY_REGEX_MATCH, parentTypeRegex));
        
        return this;
    }

    public BindingRequestBuilder subtypeOfRegex(String parentTypeRegex,Object scope)
    {
        
        requests.add(new BindingRequest(RequestType.SUBTYPE_OF_BY_REGEX_MATCH, parentTypeRegex,scope));
        
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
