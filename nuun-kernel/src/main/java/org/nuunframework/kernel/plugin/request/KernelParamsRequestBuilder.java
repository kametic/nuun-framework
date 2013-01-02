/**
 * 
 */
package org.nuunframework.kernel.plugin.request;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;


/**
 * @author Epo Jemba
 *
 */
public class KernelParamsRequestBuilder implements Builder<Collection<KernelParamsRequest>>
{
    
    private Collection<KernelParamsRequest> requests;
    
    /**
     * 
     */
    public KernelParamsRequestBuilder()
    {
        requests = new HashSet<KernelParamsRequest>();
    }
    
    
    public KernelParamsRequestBuilder optional(String keyRequested)
    {
        
        requests.add(new KernelParamsRequest(KernelParamsRequestType.OPTIONAL, keyRequested));
        
        return this;
    }

    public KernelParamsRequestBuilder mandatory(String keyRequested)
    {
        
        requests.add(new KernelParamsRequest(KernelParamsRequestType.MANDATORY, keyRequested));
        
        return this;
    }
    
    
    
    @Override
    public Collection<KernelParamsRequest> build()
    {
        return Collections.unmodifiableCollection(requests);
    }
    
    @Override
    public void reset()
    {
        requests.clear();
    }
    
    
}
