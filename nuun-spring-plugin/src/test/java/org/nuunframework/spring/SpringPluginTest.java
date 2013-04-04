package org.nuunframework.spring;

import com.google.inject.Key;
import com.google.inject.name.Names;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nuunframework.kernel.Kernel;
import org.nuunframework.spring.sample.Service1;
import org.nuunframework.spring.sample.Service1Internal;
import org.nuunframework.spring.sample.Service3;
import org.nuunframework.spring.sample.Service3Internal;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

import static org.fest.assertions.Assertions.assertThat;

public class SpringPluginTest {

    static Kernel kernel;


    @BeforeClass
    @SuppressWarnings("unchecked")
    public static void init() {
        kernel = Kernel.createKernel().withoutSpiPluginsLoader().withPlugins(UsingSpringAsDIPlugin.class, SpringPlugin.class).build();
        kernel.init();
        kernel.start();

    }

    @Test
    public void bean_declared_in_spring_should_be_accessible_from_kernel() {
        Service1 service1_by_name = kernel.getMainInjector().getInstance(Key.get(Service1Internal.class, Names.named("service1")));
        Service1 service1 = kernel.getMainInjector().getInstance(Service1.class);
        assertThat(service1).isNotNull();
        assertThat(service1).isNotNull();
        assertThat(service1.serve()).isEqualTo(Service1Internal.class.getName());
        // by default spring 
        assertThat(service1).isEqualTo(kernel.getMainInjector().getInstance(Service1.class));
        // with guice this should not work but both bindings are backed by the same applicationcontext
        assertThat(service1).isEqualTo(service1_by_name);

//      Binding by parent was removed from plugin
//      AbstractService2 service2 = kernel.getMainInjector().getInstance(AbstractService2.class);
//      assertThat ( service2).isNotNull();
//      assertThat ( service2.serve2()).isNotNull();
//      assertThat ( service2.serve2()).isEqualTo(Service2Internal.class.getName());
//      assertThat ( service2).isEqualTo(kernel.getMainInjector().getInstance(AbstractService2.class));

        Service3 service3 = kernel.getMainInjector().getInstance(Service3.class);
        assertThat(service3).isNotNull();
        assertThat(service3.serve()).isEqualTo(Service3Internal.class.getName());

//      kernel.getMainInjector().injectMembers(instance);
    }

    @Test
    public void canHandle_should_return_true_for_application_context_classes() {
        InternalDependencyInjectionProvider provider = new InternalDependencyInjectionProvider();

        ApplicationContext application = new StaticApplicationContext();

        assertThat(provider.canHandle(application.getClass())).isTrue();

    }

    @AfterClass
    public static void clear() {
        kernel.stop();
    }

}
