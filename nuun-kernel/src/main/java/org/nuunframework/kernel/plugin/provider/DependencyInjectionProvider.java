package org.nuunframework.kernel.plugin.provider;

import com.google.inject.Module;


public interface DependencyInjectionProvider
{
    
    boolean canHandle (Class<?> injectionDefinition);

    /**
     * Convert framework DI definition in nuun internal DI dependency guice module.
     *
     * @param injectionDefinition
     * @return Guice Plugin
     */
    Module convert(Object injectionDefinition);

    /**
     * This method will return a bridge from kernel to module with this DI.
     * For example a FactoryBean in the Spring world.
     *
     * @return an object able to deal with all kernel dependencies
     */
    Object kernelDIProvider ();

}
