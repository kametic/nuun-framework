package org.nuunframework.kernel.stereotype.sample;

import java.util.List;

import org.nuunframework.kernel.plugin.AbstractPlugin;

import com.google.inject.AbstractModule;

public class BugPlugin extends AbstractPlugin
{

    
    private List<String> list;

    public BugPlugin(List<String> list)
    {
        this.list = list;
    }
    
    @Override
    public String name()
    {
        return "bug";
    }
    
    @BugConcern
    public static class Module extends AbstractModule
    {
		public Module(String name , List<String> list) {
		}
        @Override
        protected void configure()
        {
        }
    }
    
    @Override
    public Object dependencyInjectionDef()
    {
        return new Module ( name() , list );
    }

}
