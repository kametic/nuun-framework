/**
 * 
 */
package org.nuunframework.kernel.plugin.dummy1;

import javax.inject.Inject;


/**
 * @author Epo Jemba
 *
 */
@MarkerSample4
public class Bean6
{
    
    @Inject
    @MarkerSample3
    private DummyService service;
    
    public Bean6( )
    {
    }
    
    public String sayHi ()
    {
        if (service != null)
        {
            return service.dummy();
        }
        else
        {
            return "no service";
        }
    }
}
