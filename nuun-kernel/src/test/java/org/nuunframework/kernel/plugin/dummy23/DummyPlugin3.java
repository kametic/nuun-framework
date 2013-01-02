/**
 * 
 */
package org.nuunframework.kernel.plugin.dummy23;

import org.nuunframework.kernel.plugin.AbstractPlugin;

/**
 * @author Epo Jemba
 *
 */
public class DummyPlugin3 extends AbstractPlugin
{

    /* (non-Javadoc)
     * @see org.nuunframework.kernel.plugin.AbstractPlugin#name()
     */
    @Override
    public String name()
    {
        return "dummyPlugin3";
    }
    
//    /* (non-Javadoc)
//     * @see org.nuunframework.kernel.plugin.AbstractPlugin#pluginsRequired()
//     */
//    @Override
//    public Collection<Class<? extends Plugin>> requiredPlugins()
//    {
//        return (Collection) convertToCollection(DummyPlugin.class);
//    }

}
