/**
 * 
 */
package org.nuunframework.kernel.plugin.request;


/**
 * @author Epo Jemba
 *
 */
public class ClasspathScanRequest
{

    public final RequestType requestType;
    public final Object objectRequested;

    public ClasspathScanRequest(RequestType requestType , Object keyRequested)
    {
        this.requestType = requestType;
        this.objectRequested = keyRequested;
        
    }
    
}
