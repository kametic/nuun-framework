/**
 * 
 */
package org.nuunframework.kernel.plugin.dummy1;

import org.nuunframework.kernel.plugins.configuration.NuunProperty;


/**
 * @author Epo Jemba
 *
 */
public class BeanWithParentType implements DummyMarker
{
    @NuunProperty("custom.name2")
    String name;
    
    public String name()
    {
        return "I am " + name; 
    }
}
