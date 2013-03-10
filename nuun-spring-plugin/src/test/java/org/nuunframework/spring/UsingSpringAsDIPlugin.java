package org.nuunframework.spring;

import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.nuunframework.spring.sample.Service3Internal;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

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
        ClassPathXmlApplicationContext parentCtx = new  ClassPathXmlApplicationContext("context.xml");
        
        StaticApplicationContext dynCtx = new StaticApplicationContext();        
        GenericBeanDefinition beanDef = new GenericBeanDefinition();
        beanDef.setBeanClass(Service3Internal.class);
        beanDef.setScope("prototype");
        dynCtx.registerBeanDefinition("service3", beanDef );
        
        dynCtx.setParent(parentCtx);        
        dynCtx.refresh();
        
        

        return dynCtx;
    }
    
}
