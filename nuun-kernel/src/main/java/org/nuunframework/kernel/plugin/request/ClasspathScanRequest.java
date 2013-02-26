/**
 * 
 */
package org.nuunframework.kernel.plugin.request;

import org.nuunframework.kernel.commons.specification.Specification;


/**
 * @author Epo Jemba
 *
 */
public class ClasspathScanRequest
{

    public final RequestType requestType;
    public final Object objectRequested;
    public final Specification<Class<?>> specification;

    public ClasspathScanRequest(RequestType requestType , Object keyRequested)
    {
        this.requestType = requestType;
        this.objectRequested = keyRequested;
        this.specification = null;
    }
    public ClasspathScanRequest(Specification<Class<?>> specification)
    {
        this.specification = specification;
        this.requestType = RequestType.VIA_SPECIFICATION;
        this.objectRequested = null;
    }
    
}
