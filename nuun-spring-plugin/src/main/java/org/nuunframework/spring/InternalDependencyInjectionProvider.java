package org.nuunframework.spring;

import com.google.inject.Module;
import org.nuunframework.kernel.plugin.provider.DependencyInjectionProvider;
import org.springframework.context.ApplicationContext;

class InternalDependencyInjectionProvider implements DependencyInjectionProvider
{

    @Override
    public boolean canHandle(Class<?> injectionDefinition)
    {
        return (ApplicationContext.class.isAssignableFrom(injectionDefinition));
    }

    @Override
    public Module convert(Object injectionDefinition)
    {
        ApplicationContext context = ApplicationContext.class.cast(injectionDefinition);
        return new SpringModule(context);
    }

    @Override
    public Object kernelDIProvider()
    {
        return null;
    }

}