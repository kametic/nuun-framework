/**
 * 
 */
package org.nuunframework.kernel.plugin.dummy1;

import org.nuunframework.kernel.plugins.configuration.NuunProperty;


/**
 * @author Epo Jemba
 *
 */
public class BeanWithCustomSuffix
{
    @NuunProperty("custom.name")
    String name;
    
    public String name()
    {
        return "I am " + name; 
    }
}
