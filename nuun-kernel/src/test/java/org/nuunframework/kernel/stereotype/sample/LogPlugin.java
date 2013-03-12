package org.nuunframework.kernel.stereotype.sample;

import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.nuunframework.kernel.plugin.AbstractPlugin;

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
    public static class Module extends AbstractModule
    {
        private String name;
		private List<String> list2;

		public Module(String name , List<String> list) {
			this.name = name;
			list2 = list;
		}
    	
    	@Override
        protected void configure()
        {
            MethodInterceptor interceptor = new ConcernInterceptor( list2, name);
            bindInterceptor(Matchers.any(), Matchers.any(), interceptor );
        }
    }
    
    @Override
    public Object dependencyInjectionDef()
    {
        return new Module(name() , list);
    }

}
