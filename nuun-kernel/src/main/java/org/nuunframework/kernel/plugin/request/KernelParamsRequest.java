/**
 * 
 */
package org.nuunframework.kernel.plugin.request;


/**
 * @author Epo Jemba
 *
 */
public class KernelParamsRequest
{

    public final KernelParamsRequestType requestType;
    public final String keyRequested;

    public KernelParamsRequest(KernelParamsRequestType requestType , String keyRequested)
    {
        this.requestType = requestType;
        this.keyRequested = keyRequested;
        
    }
    
}
