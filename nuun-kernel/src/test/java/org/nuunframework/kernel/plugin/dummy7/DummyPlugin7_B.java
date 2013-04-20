package org.nuunframework.kernel.plugin.dummy7;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Collection;

import org.nuunframework.kernel.context.InitContext;
import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.nuunframework.kernel.plugin.InitState;
import org.nuunframework.kernel.plugin.Plugin;
import org.nuunframework.kernel.plugin.request.BindingRequest;
import org.nuunframework.kernel.plugin.request.ClasspathScanRequest;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
/**
 * 
 * A 2 rounds plugin
 * 
 * @author ejemba
 *
 */
public class DummyPlugin7_B extends AbstractPlugin
{

    public DummyPlugin7_B()
    {
    }
    
    @Override
    public String name()
    {
        return "dummy-plugin-7-B";
    }

    
    @Override
    public String pluginPackageRoot()
    {
        return DummyPlugin7_B.class.getPackage().getName();
    }
    
    
    @Override
    public Object dependencyInjectionOverridingDef()
    {
        return new AbstractModule()
        {
            
            @Override
            protected void configure()
            {
                bind(String.class).annotatedWith(Names.named("dep7a")).toInstance("dep7aOVER");
            }
        };
    }
    
}
