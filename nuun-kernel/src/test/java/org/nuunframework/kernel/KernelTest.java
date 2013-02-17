/**
 * 
 */
package org.nuunframework.kernel;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nuunframework.kernel.Kernel;
import org.nuunframework.kernel.KernelException;
import org.nuunframework.kernel.context.ContextInternal;
import org.nuunframework.kernel.plugin.dummy1.Bean6;
import org.nuunframework.kernel.plugin.dummy1.Bean9;
import org.nuunframework.kernel.plugin.dummy1.BeanWithCustomSuffix;
import org.nuunframework.kernel.plugin.dummy1.BeanWithParentType;
import org.nuunframework.kernel.plugin.dummy1.DummyMarker;
import org.nuunframework.kernel.plugin.dummy1.MarkerSample4;
import org.nuunframework.kernel.plugin.dummy1.ParentClassWithCustomSuffix;
import org.nuunframework.kernel.plugin.dummy1.ParentInterfaceWithCustomSuffix;
import org.nuunframework.kernel.plugin.dummy23.DummyPlugin2;
import org.nuunframework.kernel.plugin.dummy23.DummyPlugin3;
import org.nuunframework.kernel.sample.Holder;
import org.nuunframework.kernel.sample.HolderForBeanWithParentType;
import org.nuunframework.kernel.sample.HolderForContext;
import org.nuunframework.kernel.sample.HolderForInterface;
import org.nuunframework.kernel.sample.HolderForPlugin;
import org.nuunframework.kernel.sample.HolderForPrefixWithName;
import org.nuunframework.kernel.sample.ModuleInError;
import org.nuunframework.kernel.sample.ModuleInterface;

import com.google.inject.AbstractModule;
import com.google.inject.CreationException;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * @author Epo Jemba
 * 
 */
public class KernelTest
{

    Injector      injector;

    static Kernel underTest;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void init()
    {
        underTest = Kernel.createKernel("dummy.plugin1", "WAZAAAA").build();
        try
        {
            underTest.init();
            assertThat(true).as("Should Get a Kernel Exeption for dependency problem").isFalse();
        }
        catch (KernelException ke)
        {
            assertThat(ke.getMessage()).isEqualTo("plugin dummyPlugin misses the following plugin/s as dependency/ies [class org.nuunframework.kernel.plugin.dummy23.DummyPlugin2]");
            underTest.addPlugins( DummyPlugin2.class);
            try {
                underTest.init();
                assertThat(true).as("Should Get a Kernel Exeption for dependency problem").isFalse();
            }
            catch (KernelException ke2)
            {
                assertThat(ke2.getMessage()).isEqualTo("plugin dummyPlugin2 misses the following plugin/s as dependency/ies [class org.nuunframework.kernel.plugin.dummy23.DummyPlugin3]");
                underTest.addPlugins( DummyPlugin3.class);
                underTest.init();
            }
        }

        underTest.start();
    }

    @Before
    public void before()
    {

        Module aggregationModule = new AbstractModule()
        {

            @Override
            protected void configure()
            {
                bind(Holder.class);
                bind(HolderForPlugin.class);
                bind(HolderForContext.class);
                bind(HolderForPrefixWithName.class);
                bind(HolderForBeanWithParentType.class);
            }
        };
        injector = underTest.getMainInjector().createChildInjector(aggregationModule);

    }

    @Test
    public void logger_should_be_injected()
    {
        Holder holder = injector.getInstance(Holder.class);
        assertThat(holder.getLogger()).isNotNull();
        holder.getLogger().error("MESSAGE FROM LOGGER.");
    }
    
    @Test
    public void logger_should_be_injected_with_metaannotation()
    {
        Holder holder = injector.getInstance(Holder.class);
        assertThat(holder.getLogger2()).isNotNull();
        holder.getLogger().error("MESSAGE FROM LOGGER2.");
    }

    @Test
    public void plugin_shoud_be_detected()
    {
        HolderForPlugin holder = injector.getInstance(HolderForPlugin.class);

        assertThat(holder.value).isNotNull();
        assertThat(holder.value).isEqualTo("lorem ipsum");

        assertThat(holder.dummyService).isNotNull();
        assertThat(holder.dummyService.dummy()).isEqualTo("dummyserviceinternal");

        assertThat(holder.bean6).isNotNull();
        assertThat(holder.bean6.sayHi()).isEqualTo("dummyserviceinternal2");

        assertThat(holder.bean9).isNotNull();
        assertThat(holder.bean9.value).isEqualTo("lorem ipsum");
    }

    @Test(expected = CreationException.class)
    public void plugin_shoud_be_detected_error_case()
    {
        // Bean7 is not bound to the env because of @Ignore
        injector.createChildInjector(new ModuleInError());

    }

    @Test
    public void injector_should_retrieve_context()
    {
        HolderForContext holder = injector.getInstance(HolderForContext.class);

        assertThat(holder.context).isNotNull();
        assertThat(holder.context instanceof ContextInternal).isTrue();

        ContextInternal contextInternal = (ContextInternal) holder.context;

        assertThat(contextInternal.mainInjector).isNotNull();

        assertThat(holder.context.getClassAnnotatedWith(MarkerSample4.class)).isNotEmpty();
        assertThat(holder.context.getClassAnnotatedWith(MarkerSample4.class)).hasSize(1);
        assertThat(holder.context.getClassAnnotatedWith(MarkerSample4.class)).containsOnly(Bean6.class);

//        TODO : faire pareil que pour les scans rechecher par regex
        assertThat(holder.context.getClassAnnotatedWithRegex(".*MarkerSample3")).isNotEmpty();
        assertThat(holder.context.getClassAnnotatedWithRegex(".*MarkerSample3")).hasSize(1);
        assertThat(holder.context.getClassAnnotatedWithRegex(".*MarkerSample3")).containsOnly(Bean9.class);

//        TODO : aussi rajouter parent type by regex 
        assertThat(holder.context.getClassWithParentType(DummyMarker.class)).isNotEmpty();
        assertThat(holder.context.getClassWithParentType(DummyMarker.class)).hasSize(1);
        assertThat(holder.context.getClassWithParentType(DummyMarker.class)).containsOnly(BeanWithParentType.class);

//        TODO : faire pareil que pour les scans rechecher par regex
        assertThat(holder.context.getClassTypeByRegex(".*WithCustomSuffix")).isNotEmpty();
        assertThat(holder.context.getClassTypeByRegex(".*WithCustomSuffix")).hasSize(3); // was 2
        assertThat(holder.context.getClassTypeByRegex(".*WithCustomSuffix")).containsOnly(BeanWithCustomSuffix.class, ParentClassWithCustomSuffix.class, ParentInterfaceWithCustomSuffix.class);

    }

    @Test
    public void bean_should_be_bind_by_name()
    {
        HolderForPrefixWithName holder = injector.getInstance(HolderForPrefixWithName.class);
        assertThat(holder.customBean).isNotNull();
        assertThat(holder.customBean.name()).isNotNull();
        assertThat(holder.customBean.name()).isEqualTo("I am John");

    }

    @Test
    public void bean_should_be_bind_by_parent_type()
    {
        HolderForBeanWithParentType holder = injector.getInstance(HolderForBeanWithParentType.class);
        assertThat(holder.beanWithParentType).isNotNull();
        assertThat(holder.beanWithParentType.name()).isNotNull();
        assertThat(holder.beanWithParentType.name()).isEqualTo("I am Jane");

    }

    @Test
    public void interface_can_be_injected_without_instance_if_declared_nullable()
    {
        Injector createChildInjector = injector.createChildInjector(new ModuleInterface());
        HolderForInterface holder = createChildInjector.getInstance(HolderForInterface.class);
        assertThat(holder.customBean).isNull();
    }

    @Test
    public void property_should_be_reachable_via_annotation()
    {
        Holder holder = injector.getInstance(Holder.class);

        assertThat(holder.user).isNotNull();
        assertThat(holder.user).isEqualTo("ejemba");
        assertThat(holder.id).isNotNull();
        assertThat(holder.id).isEqualTo(123l);
        assertThat(holder.password).isNotNull();
        assertThat(holder.password).isEqualTo("mypassword");

        assertThat(holder._int).isNotNull();
        assertThat(holder._int).isEqualTo(1);
        assertThat(holder._Integer).isNotNull();
        assertThat(holder._Integer).isEqualTo(1);
        assertThat(holder.__Integer).isNotNull();
        assertThat(holder.__Integer).isEqualTo("1");

        assertThat(holder._long).isNotNull();
        assertThat(holder._long).isEqualTo(2);
        assertThat(holder._Long).isNotNull();
        assertThat(holder._Long).isEqualTo(2);
        assertThat(holder.__Long).isNotNull();
        assertThat(holder.__Long).isEqualTo("2");

        assertThat(holder._short).isNotNull();
        assertThat(holder._short).isEqualTo((short) 3);
        assertThat(holder._Short).isNotNull();
        assertThat(holder._Short).isEqualTo((short) 3);
        assertThat(holder.__Short).isNotNull();
        assertThat(holder.__Short).isEqualTo("3");

        assertThat(holder._byte).isNotNull();
        assertThat(holder._byte).isEqualTo((byte) 4);
        assertThat(holder._Byte).isNotNull();
        assertThat(holder._Byte).isEqualTo((byte) 4);
        assertThat(holder.__Byte).isNotNull();
        assertThat(holder.__Byte).isEqualTo("4");

        assertThat(holder._float).isNotNull();
        assertThat(holder._float).isEqualTo(5.5f);
        assertThat(holder._Float).isNotNull();
        assertThat(holder._Float).isEqualTo(5.5f);
        assertThat(holder.__Float).isNotNull();
        assertThat(holder.__Float).isEqualTo("5.5");

        assertThat(holder._double).isNotNull();
        assertThat(holder._double).isEqualTo(6.6d);
        assertThat(holder._Double).isNotNull();
        assertThat(holder._Double).isEqualTo(6.6d);
        assertThat(holder.__Double).isNotNull();
        assertThat(holder.__Double).isEqualTo("6.6");

        assertThat(holder._char).isNotNull();
        assertThat(holder._char).isEqualTo('g');
        assertThat(holder._Character).isNotNull();
        assertThat(holder._Character).isEqualTo('g');
        assertThat(holder.__Character).isNotNull();
        assertThat(holder.__Character).isEqualTo("g");

        assertThat(holder._booleanTrue).isNotNull();
        assertThat(holder._booleanTrue).isEqualTo(true);
        assertThat(holder._BooleanTrue).isNotNull();
        assertThat(holder._BooleanTrue).isEqualTo(true);
        assertThat(holder._BooleanTrue).isNotNull();
        assertThat(holder.__BooleanTrue).isEqualTo("true");

        assertThat(holder._booleanFalse).isNotNull();
        assertThat(holder._booleanFalse).isEqualTo(false);
        assertThat(holder._BooleanFalse).isNotNull();
        assertThat(holder._BooleanFalse).isEqualTo(false);
        assertThat(holder._BooleanFalse).isNotNull();
        assertThat(holder.__BooleanFalse).isEqualTo("false");
        // use of converter
        assertThat(holder.__special).isNotNull();
        assertThat(holder.__special.content ).hasSize(7);
        assertThat(holder.__special.content ).containsOnly("epo" , "jemba", "the" , "master" , "of" , "the" , "universe");
        
        assertThat(holder.__nothing).isNotNull();
        assertThat(holder.__nothing).isEqualTo("");

    }

    @AfterClass
    public static void clear()
    {
        underTest.stop();
    }

}
