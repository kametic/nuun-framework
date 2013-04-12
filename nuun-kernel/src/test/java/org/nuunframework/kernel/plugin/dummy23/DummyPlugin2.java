/**
 * 
 */
package org.nuunframework.kernel.plugin.dummy23;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Collection;

import org.nuunframework.kernel.context.InitContext;
import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.nuunframework.kernel.plugin.InitState;
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
    public Collection<Class<? extends Plugin>> requiredPlugins()
    {
        return (Collection) collectionOf(DummyPlugin3.class);
    }
    
    
    @Override
    public InitState init(InitContext initContext)
    {
        assertThat( initContext.pluginsRequired() ).isNotNull();
        assertThat( initContext.pluginsRequired() ).hasSize(1);
        assertThat( initContext.pluginsRequired().iterator().next().getClass() ).isEqualTo(DummyPlugin3.class);
        return InitState.INITIALIZED;
    }

}
