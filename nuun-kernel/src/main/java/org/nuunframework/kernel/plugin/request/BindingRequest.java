/**
 * 
 */
package org.nuunframework.kernel.plugin.request;


/**
 * @author Epo Jemba
 *
 */
public class BindingRequest
{

    public final RequestType requestType;
    public final Object requestedObject;
    public final Object requestedScope;

    public BindingRequest(RequestType requestType , Object keyRequested)
    {
        this( requestType , keyRequested , null );
    }

    public BindingRequest(RequestType requestType , Object keyRequested , Object requestedScope)
    {
        this.requestType = requestType;
        this.requestedObject = keyRequested;
        this.requestedScope = requestedScope;
    }
    
}
