package org.nuunframework.spring;

import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UsingSpringAsDIPlugin extends AbstractPlugin
{

    @Override
    public String name()
    {
        return "using-spring-as-di-plugin";
    }
    
    
    @Override
    public Object dependencyInjectionDef()
    {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");
        
        return ctx;
    }

}
