/**
 * 
 */
package org.nuunframework.kernel.plugin.dummy23;

import java.util.Collection;

import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.nuunframework.kernel.plugin.Plugin;


/**
 * @author Epo Jemba
 * 
 */
public class DummyPlugin2 extends AbstractPlugin
{

    /*
     * (non-Javadoc)
     * 
     * @see org.nuunframework.kernel.plugin.AbstractPlugin#name()
     */
    @Override
    public String name()
    {
        return "dummyPlugin2";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nuunframework.kernel.plugin.AbstractPlugin#pluginsRequired()
     */
    @SuppressWarnings({
        "unchecked", "rawtypes"
    })
    @Override
    public Collection<Class<? extends Plugin>> pluginDependenciesRequired()
    {
        return (Collection) collectionOf(DummyPlugin3.class);
    }

}
