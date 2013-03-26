package org.nuunframework.kernel.internal.scanner.sample;

import org.nuunframework.kernel.annotations.KernelModule;

import com.google.inject.AbstractModule;

@Ignore
@KernelModule
public class MyModule3 extends AbstractModule
{
    // Exemple with custom ignore annotation 
    
    @Override
    protected void configure()
    {
    }

}
