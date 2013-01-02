/**
 * 
 */
package org.nuunframework.kernel.plugin.dummy1;

import java.util.Collection;

import javax.annotation.Nullable;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.util.Providers;


/**
 * @author Epo Jemba
 *
 */
public class DummyModule extends AbstractModule
{

    
    
    
    private final Collection<Class<?>> classes;


    /**
     * 
     */
    public DummyModule(Collection<Class<?>> classes)
    {
        this.classes = classes;
    }
    
    
    /* (non-Javadoc)
     * @see com.google.inject.AbstractModule#configure()
     */
    @SuppressWarnings({
            "rawtypes", "unchecked"
    })
    @Override
    protected void configure()
    {
        bind(DummyService.class).to(DummyServiceInternal.class);
        bind(DummyService.class).annotatedWith(MarkerSample3.class).to(DummyServiceInternal2.class);
        Provider ofNull = Providers.of(null);
        for (Class<?> klass : classes)
        {
            System.err.println("logger " + klass);
            if (klass.getAnnotation(Nullable.class)== null )
            {
                bind(klass);
            }
            else
            {
                bind(klass).toProvider( ofNull);
            }
        }
    }

}
