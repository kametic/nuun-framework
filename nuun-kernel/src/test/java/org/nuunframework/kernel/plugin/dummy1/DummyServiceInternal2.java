/**
 * 
 */
package org.nuunframework.kernel.plugin.dummy1;


/**
 * @author Epo Jemba
 *
 */
public class DummyServiceInternal2 implements DummyService
{
   
    /**
     * 
     */
    public DummyServiceInternal2()
    {
    }
    
    /* (non-Javadoc)
     * @see org.nuunframework.kernel.plugin.DummyService#dummy()
     */
    @Override
    public String dummy()
    {
        return "dummyserviceinternal2";
    }

}
