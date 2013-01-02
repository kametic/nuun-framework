/**
 * 
 */
package org.nuunframework.kernel.plugin.dummy1;

import org.nuunframework.kernel.plugins.configuration.Property;


/**
 * @author Epo Jemba
 *
 */
public class BeanWithCustomSuffix
{
    @Property("custom.name")
    String name;
    
    public String name()
    {
        return "I am " + name; 
    }
}
