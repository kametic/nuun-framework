package org.nuunframework.kernel.stereotype.sample;

import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.nuunframework.kernel.plugin.AbstractPlugin;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class CachePlugin extends AbstractPlugin
{

    
    private List<String> list;

    public CachePlugin(List<String> list)
    {
        this.list = list;
    }
    
    @Override
    public String name()
    {
        return "cache";
    }
    
    @CacheConcern
    class Module extends AbstractModule
    {
        @Override
        protected void configure()
        {
            MethodInterceptor interceptor = new ConcernInterceptor(list,  "cache");
            bindInterceptor(Matchers.any(), Matchers.any(), interceptor );
        }
    }
    
    @Override
    public Object dependencyInjectionDef()
    {
        return new Module();
    }

}
