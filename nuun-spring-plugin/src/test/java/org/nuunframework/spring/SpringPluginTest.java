/**
 * Copyright (C) 2013 Kametic <epo.jemba@kametic.com>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * or any later version
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.nuunframework.spring;

import com.google.inject.Key;
import com.google.inject.name.Names;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nuunframework.kernel.Kernel;
import org.nuunframework.spring.sample.AbstractService2;
import org.nuunframework.spring.sample.Service1;
import org.nuunframework.spring.sample.Service1Internal;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

import static org.fest.assertions.Assertions.assertThat;

public class SpringPluginTest {

    static Kernel kernel;


    @BeforeClass
    @SuppressWarnings("unchecked")
    public static void setup() {
        kernel = Kernel.createKernel()
                .withoutSpiPluginsLoader()
                .withPlugins(UsingSpringAsDIPlugin.class, SpringPlugin.class)
                .build();

        kernel.init();
        kernel.start();

    }

    @AfterClass
    public static void teardown() {
        kernel.stop();
    }

    @Test
    public void spring_bean_should_be_injectable_by_type_and_name() {
        Service1 service1_by_name = kernel.getMainInjector().getInstance(Key.get(Service1Internal.class, Names.named("service1")));
        assertThat(service1_by_name).isNotNull();
    }

    @Test
    public void spring_bean_should_be_injectable_by_interface_and_name() {
        Service1 service1 = kernel.getMainInjector().getInstance(Key.get(Service1.class, Names.named("service1")));
        assertThat(service1).isNotNull();
    }

    @Test
    public void spring_bean_should_be_injectable_by_parent_type_and_name() {
        AbstractService2 service2 = kernel.getMainInjector().getInstance(Key.get(AbstractService2.class, Names.named("service2")));
        assertThat(service2).isNotNull();
    }

//    @Test
//    public void spring_bean_should_be_injectable_by_type() {
//        assertThat(service1.serve()).isEqualTo(Service1Internal.class.getName());
//        // by default spring
//        assertThat(service1).isEqualTo(kernel.getMainInjector().getInstance(Service1.class));
//        // with guice this should not work but both bindings are backed by the same applicationcontext
//        assertThat(service1).isEqualTo(service1_by_name);
//
//        AbstractService2 service2 = kernel.getMainInjector().getInstance(AbstractService2.class);
//        assertThat(service2).isNotNull();
//        assertThat(service2.serve2()).isNotNull();
//        assertThat(service2.serve2()).isEqualTo(Service2Internal.class.getName());
//        assertThat(service2).isEqualTo(kernel.getMainInjector().getInstance(AbstractService2.class));
//
//        Service3 service3 = kernel.getMainInjector().getInstance(Service3.class);
//        assertThat(service3).isNotNull();
//        assertThat(service3.serve()).isEqualTo(Service3Internal.class.getName());
//
////      kernel.getMainInjector().injectMembers(instance);
//    }

    @Test
    public void canHandle_should_return_true_for_application_context_classes() {
        InternalDependencyInjectionProvider provider = new InternalDependencyInjectionProvider();

        ApplicationContext application = new StaticApplicationContext();

        assertThat(provider.canHandle(application.getClass())).isTrue();

    }

    @Test
    public void canHandle_should_return_true_for_bean_factory_classes() {
        InternalDependencyInjectionProvider provider = new InternalDependencyInjectionProvider();

        ApplicationContext application = new StaticApplicationContext();

        assertThat(provider.canHandle(application.getAutowireCapableBeanFactory().getClass())).isTrue();

    }

    @AfterClass
    public static void clear() {
        kernel.stop();
    }

}
