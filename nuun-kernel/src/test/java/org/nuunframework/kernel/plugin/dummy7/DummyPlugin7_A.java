package org.nuunframework.kernel.plugin.dummy7;

import org.nuunframework.kernel.plugin.AbstractPlugin;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class DummyPlugin7_A extends AbstractPlugin
{

    @Override
    public String name()
    {
        return "dummy-plugin-7-A";
    }
    
    @Override
    public String toString()
    {
        return "A";
    }
    
    @Override
    public Object dependencyInjectionDef()
    {
        return new AbstractModule()
        {
            
            @Override
            protected void configure()
            {
                bind(String.class).annotatedWith(Names.named("dep7a")).toInstance("dep7a");
                bind(String.class).annotatedWith(Names.named("dep7b")).toInstance("dep7b");
            }
        };
    }

}
