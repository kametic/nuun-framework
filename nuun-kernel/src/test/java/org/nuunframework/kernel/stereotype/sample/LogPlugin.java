package org.nuunframework.kernel.stereotype.sample;

import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.nuunframework.kernel.stereotype.sample.CachePlugin.Module;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class LogPlugin extends AbstractPlugin
{

    
    private List<String> list;

    public LogPlugin(List<String> list)
    {
        this.list = list;
    }
    
    @Override
    public String name()
    {
        return "log";
    }
    
    @LogConcern
    class Module extends AbstractModule
    {
        @Override
        protected void configure()
        {
            MethodInterceptor interceptor = new ConcernInterceptor( list , name());
            bindInterceptor(Matchers.any(), Matchers.any(), interceptor );
        }
    }
    
    @Override
    public Object dependencyInjectionDef()
    {
        return new Module();
    }

}
