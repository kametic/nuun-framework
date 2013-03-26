package org.nuunframework.kernel.plugin.dummy6;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Collection;

import org.nuunframework.kernel.context.InitContext;
import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.nuunframework.kernel.plugin.Plugin;

public class DummyPlugin6_B extends AbstractPlugin
{

    @Override
    public String name()
    {
        return "dummy-plugin-6-B";
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Collection<Class<? extends Plugin>> dependentPlugins()
    {
        return collectionOf(  DummyPlugin6_D.class , DummyPlugin6_C.class);
    }
    
    
    @Override
    public void init(InitContext initContext)
    {
        Collection<? extends Plugin> dependentPlugins = initContext.dependentPlugins();
        assertThat(dependentPlugins).isNotNull();
        assertThat(dependentPlugins).hasSize(2);
        
        for (Plugin plugin : dependentPlugins)
        {
            if (DummyPlugin6_D.class.isAssignableFrom(plugin.getClass())) 
            {
                DummyPlugin6_D.class.cast(plugin).setInternal(true);
            }
            if (DummyPlugin6_C.class.isAssignableFrom(plugin.getClass())) 
            {
                DummyPlugin6_C.class.cast(plugin).setInternal(true);
            }
        }
        
    }

    @Override
    public String toString()
    {
        return "B";
    }
    
}
