package org.nuunframework.kernel.plugin.dummy6;

import org.nuunframework.kernel.plugin.AbstractPlugin;

public class DummyPlugin6_C extends AbstractPlugin
{

    
    @Override
    public String name()
    {
        return "dummy-plugin-6-C";
    }

    
    @Override
    public String toString()
    {
        return "C";
    }

    private boolean internal = false;

    public boolean isInternal()
    {
        return internal;
    }


    public void setInternal(boolean internal)
    {
        this.internal = internal;
    }
    
    
    
}
