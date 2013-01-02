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
    public final Object objectRequested;

    public BindingRequest(RequestType requestType , Object keyRequested)
    {
        this.requestType = requestType;
        this.objectRequested = keyRequested;
        
    }
    
}
