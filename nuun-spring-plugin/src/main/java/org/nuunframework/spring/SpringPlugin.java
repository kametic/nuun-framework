package org.nuunframework.spring;

import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.nuunframework.kernel.plugin.provider.DependencyInjectionProvider;

public class SpringPlugin extends AbstractPlugin
{

    private DependencyInjectionProvider provider ;
    
    private boolean byName = true;
    private boolean byInterfaces = true;
    private boolean byParent = true;
    
    @Override
    public String name()
    {
        return "nuun-spring-plugin";
    }
    
    @Override
    public DependencyInjectionProvider dependencyInjectionProvider()
    {
        if (null == provider)
        {
            provider = new InternalDependencyInjectionProvider(byName, byInterfaces , byParent);
        }
        
        return provider;
    }
}
