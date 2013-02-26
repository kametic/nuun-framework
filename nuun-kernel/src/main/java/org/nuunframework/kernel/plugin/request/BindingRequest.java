/**
 * 
 */
package org.nuunframework.kernel.plugin.request;

import org.nuunframework.kernel.commons.specification.Specification;


/**
 * @author Epo Jemba
 *
 */
public class BindingRequest
{

    public final RequestType requestType;
    public final Object requestedObject;
    public final Object requestedScope;
    public final Specification<Class<?>> specification;

    public BindingRequest(RequestType requestType , Object keyRequested)
    {
        this( requestType , keyRequested , null , null);
    }

    public BindingRequest(RequestType requestType ,  Object requestedScope , Specification<Class<?>> specification)
    {
        this( requestType ,  null,  requestedScope ,  specification);
    }
    
    public BindingRequest(RequestType requestType ,  Specification<Class<?>> specification)
    {
        this( requestType ,  null,  null ,  specification);
    }
    
    public BindingRequest(RequestType requestType , Object keyRequested , Object requestedScope , Specification<Class<?>> specification)
    {
        this.requestType = requestType;
        this.requestedObject = keyRequested;
        this.requestedScope = requestedScope;
        this.specification = specification;
    }
    
}
