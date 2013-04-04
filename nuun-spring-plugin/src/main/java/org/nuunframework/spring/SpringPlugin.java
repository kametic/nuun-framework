package org.nuunframework.spring;

import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.nuunframework.kernel.plugin.provider.DependencyInjectionProvider;

public class SpringPlugin extends AbstractPlugin {
    @Override
    public String name() {
        return "nuun-spring-plugin";
    }

    @Override
    public DependencyInjectionProvider dependencyInjectionProvider() {
        return new InternalDependencyInjectionProvider();
    }
}
