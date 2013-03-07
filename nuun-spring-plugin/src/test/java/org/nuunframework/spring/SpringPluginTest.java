package org.nuunframework.spring;

import static org.fest.assertions.Assertions.assertThat;

import org.fest.assertions.Assertions;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nuunframework.kernel.Kernel;
import org.nuunframework.spring.sample.AbstractService2;
import org.nuunframework.spring.sample.Service1;
import org.nuunframework.spring.sample.Service1Internal;
import org.nuunframework.spring.sample.Service2Internal;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

import com.google.inject.Key;
import com.google.inject.name.Names;

public class SpringPluginTest
{

    static Kernel kernel;
    
    
    
    
    @BeforeClass
    public static void init()
    {
        kernel = Kernel.createKernel("dummy.plugin1", "WAZAAAA").withoutSpiPluginsLoader().withPlugins(UsingSpringAsDIPlugin.class , SpringPlugin.class ).build();
        kernel.init();
        kernel.start();
        
    }
    
    @Test
    public void bean_declared_in_spring_should_be_accessible_from_kernel ()
    {
        Service1 service1 = kernel.getMainInjector().getInstance(Service1.class);
        Service1 service1_by_name  = kernel.getMainInjector().getInstance( Key.get(Service1Internal.class, Names.named("service1")) );
        assertThat ( service1).isNotNull();
        assertThat ( service1.serve()).isEqualTo(Service1Internal.class.getName());
        // by default spring 
        assertThat ( service1 ).isEqualTo(kernel.getMainInjector().getInstance(Service1.class));
        // with guice this should not work but both bindings are backed by the same applicationcontext
        assertThat ( service1 ).isEqualTo(service1_by_name );
        
        AbstractService2 service2 = kernel.getMainInjector().getInstance(AbstractService2.class); 
        assertThat ( service2).isNotNull();
        assertThat ( service2.serve2()).isNotNull();
        assertThat ( service2.serve2()).isEqualTo(Service2Internal.class.getName());
        assertThat ( service2).isEqualTo(kernel.getMainInjector().getInstance(AbstractService2.class));
    }
    
    @Test
    public void canHandle_should_return_true_for_application_context_classes()
    {
        InternalDependencyInjectionProvider provider = new InternalDependencyInjectionProvider(true,false,false);
        
        ApplicationContext application = new StaticApplicationContext();
        
        assertThat( provider.canHandle( application.getClass() ) ).isTrue();
        
    }

    @AfterClass
    public static void clear()
    {
        kernel.stop();
    }
    
}
