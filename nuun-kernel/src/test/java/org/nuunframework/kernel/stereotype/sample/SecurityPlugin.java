package org.nuunframework.kernel.stereotype.sample;

import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.nuunframework.kernel.plugin.AbstractPlugin;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class SecurityPlugin extends AbstractPlugin
{

    
    private List<String> list;

    public SecurityPlugin(List<String> list)
    {
        this.list = list;
    }
    
    @Override
    public String name()
    {
        return "security";
    }
    
    @SecurityConcern
    public static class Module extends AbstractModule
    {
    	private List<String> list;
		private String name;
    	
		public Module(String name , List<String> list) {
			this.name = name;
			this.list = list;
		}
        @Override
        protected void configure()
        {
            MethodInterceptor interceptor = new ConcernInterceptor( list , name);
            bindInterceptor(Matchers.any(), Matchers.any(), interceptor );
        }
    }
    
    @Override
    public Object dependencyInjectionDef()
    {
        return new Module(name() , list);
    }

}
