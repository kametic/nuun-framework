package org.nuunframework.kernel.plugin.dummy7;

import org.nuunframework.kernel.annotations.KernelModule;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

@KernelModule(overriding=true)
public class Module7 extends AbstractModule
{

    @Override
    protected void configure()
    {
        bind(String.class).annotatedWith(Names.named("dep7b")).toInstance("dep7bOVER");
    }

}
