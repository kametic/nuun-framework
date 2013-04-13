package org.nuunframework.kernel.plugin.dummy6;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Collection;

import org.nuunframework.kernel.context.InitContext;
import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.nuunframework.kernel.plugin.InitState;
import org.nuunframework.kernel.plugin.Plugin;
import org.nuunframework.kernel.plugin.request.BindingRequest;
import org.nuunframework.kernel.plugin.request.ClasspathScanRequest;
/**
 * 
 * A 2 rounds plugin
 * 
 * @author ejemba
 *
 */
public class DummyPlugin6_B extends AbstractPlugin
{

    public DummyPlugin6_B()
    {
    }
    
    @Override
    public String name()
    {
        return "dummy-plugin-6-B";
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Collection<Class<? extends Plugin>> dependentPlugins()
    {
        if (roundEnvironment != null && roundEnvironment.firstRound() )
        {
            return collectionOf(  DummyPlugin6_D.class , DummyPlugin6_C.class);
        }
        else
        {
            return super.dependentPlugins();
        }
    }
    
    
    @Override
    public Collection<BindingRequest> bindingRequests()
    {
        if (roundEnvironment.roundNumber() == 3 )
        {
            return bindingRequestsBuilder().annotationType(Marker66.class).build();
        }
        return super.bindingRequests();
    }
    
    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests()
    {
        
        if (roundEnvironment.roundNumber() == 4 )
        {
            return classpathScanRequestBuilder().annotationType(Marker6.class).build();
            
        }
        
        return super.classpathScanRequests();
    }
    
    
    @Override
    public InitState init(InitContext initContext)
    {
        if (roundEnvironment.roundNumber() != 5 )
        {
            Collection<? extends Plugin> dependentPlugins = initContext.dependentPlugins();
            assertThat(dependentPlugins).isNotNull();
            if (roundEnvironment.firstRound())
            {
                assertThat(dependentPlugins).hasSize(2);
            }
            else
            {
                assertThat(dependentPlugins).hasSize(0);
            }
            
            
            
            if (roundEnvironment.roundNumber() == 4 )
            {
                Collection<Class<?>> collection = initContext.scannedClassesByAnnotationClass().get(Marker6.class);
                assertThat(collection).hasSize(2);
            }
            else
            {
                Collection<Class<?>> collection = initContext.scannedClassesByAnnotationClass().get(Marker6.class);
                assertThat(collection).isNullOrEmpty();
            }
            
            
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
        
            return InitState.NON_INITIALIZED;
        } 
        else 
        {
            return InitState.INITIALIZED;
        }
        
    }

    @Override
    public String toString()
    {
        return "B";
    }
    
}
