package org.nuunframework.kernel.sample;

import com.google.inject.AbstractModule;

public class ModuleInterface extends AbstractModule
{

    /*
     * (non-Javadoc)
     * 
     * @see com.google.inject.AbstractModule#configure()
     */
    @Override
    protected void configure()
    {
        bind(HolderForInterface.class);
    }
}