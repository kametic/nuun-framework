package org.nuunframework.web;

import org.nuunframework.kernel.plugin.AbstractPlugin;

import com.google.inject.servlet.WorkAroundModule;

public class NuunWebPlugin extends AbstractPlugin
{

    private WorkAroundModule module = null;

    @Override
    public String name()
    {
        return "nuun-web-plugin";
    }

    @Override
    public Object dependencyInjectionDef()
    {

        if (module == null)
        {
            module = new WorkAroundModule();
        }

        return module;
    }

}
