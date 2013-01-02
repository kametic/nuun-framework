/**
 * 
 */
package org.nuunframework.kernel.plugin.dummy1;


/**
 * @author Epo Jemba
 *
 */
public class DummyServiceInternal implements DummyService
{
   
    /**
     * 
     */
    public DummyServiceInternal()
    {
    }
    
    /* (non-Javadoc)
     * @see org.nuunframework.kernel.plugin.DummyService#dummy()
     */
    @Override
    public String dummy()
    {
        return "dummyserviceinternal";
    }

}
