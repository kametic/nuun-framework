package org.nuunframework.spring;

import org.nuunframework.kernel.plugin.provider.DependencyInjectionProvider;
import org.springframework.context.ApplicationContext;

import com.google.inject.Module;

class InternalDependencyInjectionProvider implements DependencyInjectionProvider
{

    private final boolean byName;
    private final boolean byInterfaces;
    private final boolean byParent;
    
    InternalDependencyInjectionProvider(boolean byName, boolean byInterfaces , boolean byParent )
    {
        this.byName = byName;
        this.byInterfaces = byInterfaces;
        this.byParent = byParent;
    }   
    
    @Override
    public boolean canHandle(Class<?> injectionDefinition)
    {
        return (ApplicationContext.class.isAssignableFrom(injectionDefinition));
    }

    @Override
    public Module convert(Object injectionDefinition)
    {
        
        ApplicationContext context = ApplicationContext.class.cast(injectionDefinition);
        SpringModule module = new SpringModule(context,this.byName , this.byInterfaces , this.byParent);
        
        return module;
    }

    @Override
    public Object kernelDIProvider()
    {
        return null;
    }
    
}