package org.nuunframework.kernel.internal.scanner.sample;

import org.nuunframework.kernel.annotations.KernelModule;

import com.google.inject.AbstractModule;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

@KernelModule
public class MyModule1 extends AbstractModule
{

    @Override
    protected void configure()
    {
        bind(String.class).annotatedWith(Names.named("kernelmodule")).toInstance("Hi!");
    }

}
