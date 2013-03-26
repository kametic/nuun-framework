package org.nuunframework.kernel.internal.scanner.sample;

import org.nuunframework.kernel.annotations.Ignore;
import org.nuunframework.kernel.annotations.KernelModule;

import com.google.inject.AbstractModule;

@Ignore
@KernelModule
public class MyModule2 extends AbstractModule
{

    @Override
    protected void configure()
    {
        
    }

}
