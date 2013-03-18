/**
 * 
 */
package org.nuunframework.kernel.plugin.request;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.nuunframework.kernel.commons.specification.Specification;
import org.nuunframework.kernel.plugin.request.builders.BindingRequestBuilderOptionsBuildMain;


/**
 *
 *  
 * 
 * @author Epo Jemba
 *
 */
public class BindingRequestBuilder implements BindingRequestBuilderOptionsBuildMain
{
    
    private Collection<BindingRequest> requests;
    private BindingRequest currentBindingRequest = null;
    
    public BindingRequestBuilder()
    {
        requests = new HashSet<BindingRequest>();
    }
    
    
    // VIA SPECIFICATION
    @Override
    public BindingRequestBuilderOptionsBuildMain specification(Specification<Class<?>> specification)
    {
        
        requests.add(currentBindingRequest = new BindingRequest(RequestType.VIA_SPECIFICATION, null,specification));
        
        return this;
    }

    // ANNOTATION TYPE
    @Override
    public BindingRequestBuilderOptionsBuildMain annotationType(Class<? extends Annotation> annotationTypeRequested)
    {
        requests.add(currentBindingRequest = new BindingRequest(RequestType.ANNOTATION_TYPE, annotationTypeRequested));
        return this;
    }

    @Override
    public BindingRequestBuilderOptionsBuildMain metaAnnotationType(Class<? extends Annotation> metaAnnotationTypeRequested)
    {
        requests.add(currentBindingRequest = new BindingRequest(RequestType.META_ANNOTATION_TYPE, metaAnnotationTypeRequested));
        return this;
    }
 
    @Override
    public BindingRequestBuilderOptionsBuildMain annotationRegex(String annotationRegex)
    {
        requests.add(currentBindingRequest = new BindingRequest(RequestType.ANNOTATION_REGEX_MATCH, annotationRegex));
        return this;
    }

    @Override
    public BindingRequestBuilderOptionsBuildMain metaAnnotationRegex(String metaAnnotationRegex)  {
        requests.add(currentBindingRequest = new BindingRequest(RequestType.META_ANNOTATION_REGEX_MATCH, metaAnnotationRegex));
        return this;
    }

    // SUBTYPE_OF_BY_CLASS
    
    /**
     * ask for a binding based on direct subtype of parentTypeRequested. 
     * 
     * @param parentTypeRequested
     * @return
     */
    @Override
    public BindingRequestBuilderOptionsBuildMain subtypeOf(Class<?> parentTypeRequested)
    {
        requests.add(currentBindingRequest = new BindingRequest(RequestType.SUBTYPE_OF_BY_CLASS, parentTypeRequested));
        
        return this;
    }
    
    /**
     * ask for a binding based on direct subtype of parentTypeRequested. 
     * 
     * @param ancestorTypeRequested
     * @return
     */
    @Override
    public BindingRequestBuilderOptionsBuildMain descendentTypeOf(Class<?> ancestorTypeRequested)
    {
        requests.add(currentBindingRequest = new BindingRequest(RequestType.SUBTYPE_OF_BY_TYPE_DEEP, ancestorTypeRequested));
        
        return this;
    }
    
    // SUBTYPE_OF_BY_REGEX_MATCH    
    @Override
    public BindingRequestBuilderOptionsBuildMain subtypeOfRegex(String parentTypeRegex)
    {
        
        requests.add(currentBindingRequest = new BindingRequest(RequestType.SUBTYPE_OF_BY_REGEX_MATCH, parentTypeRegex));
        
        return this;
    }

//    public BindingRequestBuilderOptionsBuildMain subtypeOfRegex(String parentTypeRegex,Object scope)
//    {
//        
//        requests.add(currentBindingRequest = new BindingRequest(RequestType.SUBTYPE_OF_BY_REGEX_MATCH, parentTypeRegex,scope,null));
//        
//        return this;
//    }
    
    @Override
    public BindingRequestBuilderOptionsBuildMain withConstraint(Object constraint)
    {
        currentBindingRequest.requestedConstraint = constraint;
        return this;
    }

    @Override
    public BindingRequestBuilderOptionsBuildMain withScope(Object scope)
    {
        currentBindingRequest.requestedScope = scope;
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
