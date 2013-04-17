/**
 * 
 */
package org.nuunframework.kernel;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nuunframework.kernel.Kernel.AliasMap;
import org.nuunframework.kernel.context.ContextInternal;
import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.nuunframework.kernel.plugin.Plugin;
import org.nuunframework.kernel.plugin.dummy1.Bean6;
import org.nuunframework.kernel.plugin.dummy1.Bean9;
import org.nuunframework.kernel.plugin.dummy1.BeanWithCustomSuffix;
import org.nuunframework.kernel.plugin.dummy1.BeanWithParentType;
import org.nuunframework.kernel.plugin.dummy1.DummyMarker;
import org.nuunframework.kernel.plugin.dummy1.DummyPlugin;
import org.nuunframework.kernel.plugin.dummy1.MarkerSample4;
import org.nuunframework.kernel.plugin.dummy1.ParentClassWithCustomSuffix;
import org.nuunframework.kernel.plugin.dummy1.ParentInterfaceWithCustomSuffix;
import org.nuunframework.kernel.plugin.dummy23.DummyPlugin2;
import org.nuunframework.kernel.plugin.dummy23.DummyPlugin3;
import org.nuunframework.kernel.plugin.dummy4.DummyPlugin4;
import org.nuunframework.kernel.plugin.dummy4.Pojo1;
import org.nuunframework.kernel.plugin.dummy4.Pojo2;
import org.nuunframework.kernel.plugin.dummy5.DescendantFromClass;
import org.nuunframework.kernel.plugin.dummy5.DummyPlugin5;
import org.nuunframework.kernel.plugin.dummy5.ParentClass;
import org.nuunframework.kernel.plugin.dummy5.ToFind;
import org.nuunframework.kernel.plugin.dummy5.ToFind2;
import org.nuunframework.kernel.sample.Holder;
import org.nuunframework.kernel.sample.HolderForBeanWithParentType;
import org.nuunframework.kernel.sample.HolderForContext;
import org.nuunframework.kernel.sample.HolderForInterface;
import org.nuunframework.kernel.sample.HolderForPlugin;
import org.nuunframework.kernel.sample.HolderForPrefixWithName;
import org.nuunframework.kernel.sample.ModuleInError;
import org.nuunframework.kernel.sample.ModuleInterface;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.ConfigurationException;
import com.google.inject.CreationException;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * @author Epo Jemba
 * 
 */
public class KernelTest
{
    static Logger logger = LoggerFactory.getLogger(KernelTest.class);
    
    Injector      injector;

    static Kernel underTest;
    static DummyPlugin4 plugin4 = new DummyPlugin4();

    static long start;
    static long end;
    
    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void init()
    {
        start = System.currentTimeMillis();
//        underTest = Kernel.createKernel(DummyPlugin.ALIAS_DUMMY_PLUGIN1 , "WAZAAAA", DummyPlugin.NUUNROOTALIAS , "").build();
        underTest = Kernel.createKernel(DummyPlugin.ALIAS_DUMMY_PLUGIN1 , "WAZAAAA", DummyPlugin.NUUNROOTALIAS , "internal,"+KernelTest.class.getPackage().getName()).build();
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
                underTest.addPlugins( plugin4);
                underTest.addPlugins( DummyPlugin5.class);
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
    public void classpath_scan_by_specification_should_work()
    {
        assertThat(plugin4.collection).isNotNull();
        assertThat(plugin4.collection).isNotEmpty();
        assertThat(plugin4.collection).hasSize(1);
        assertThat(plugin4.collection).containsOnly(Pojo1.class);
    }
    
    
    
    
    @Test
    public void binding_by_specification_should_work ()
    {
        assertThat( injector.getInstance(Pojo2.class) ).isNotNull();
        // we check for the scope
        assertThat(injector.getInstance(Pojo2.class)).isEqualTo(injector.getInstance(Pojo2.class));
        
        try 
        {
            assertThat( injector.getInstance(Pojo1.class) ).isNull();
            fail("Pojo1 should not be injector");
        } catch (ConfigurationException ce)
        {
            String nl = System.getProperty("line.separator");
            assertThat(ce.getMessage()).isEqualTo("Guice configuration errors:"+nl
                    +nl
                    +"1) Explicit bindings are required and org.nuunframework.kernel.plugin.dummy4.Pojo1 is not explicitly bound."+nl
                    + "  while locating org.nuunframework.kernel.plugin.dummy4.Pojo1"+nl
                    +nl
                    +"1 error"
                    );
        }
        
    }

    @Test
    public void binding_by_metaannotation_should_work ()
    {
        ToFind tofind = injector.getInstance(ToFind.class);
        assertThat( tofind).isNotNull ();
        // singleton
        assertThat( tofind).isEqualTo(injector.getInstance(ToFind.class));
    }

    @Test
    public void binding_by_metaannotationregex_should_work ()
    {
        ToFind2 tofind = injector.getInstance(ToFind2.class);
        assertThat( tofind).isNotNull ();
        // singleton
        assertThat( tofind).isEqualTo(injector.getInstance(ToFind2.class));
    }
    
    @Test
    public void binding_by_ancestor_should_work ()
    {
        ParentClass instance = injector.getInstance(ParentClass.class);
        DescendantFromClass instance2 = injector.getInstance(DescendantFromClass.class);
        assertThat( instance).isNotNull ();
        assertThat( instance2).isNotNull ();
        // scope SINGLETON has been registered
        assertThat( instance).isEqualTo(injector.getInstance(ParentClass.class));        
        assertThat( instance2).isEqualTo(injector.getInstance(DescendantFromClass.class));
        
        
        
//        assertThat( injector.getInstance(ParentInterface.class)).isNotNull ();
//        assertThat( injector.getInstance(DescendantFromInterface.class)).isNotNull ();
        
        try 
        {
            assertThat( injector.getInstance(Pojo1.class) ).isNull();
            fail("Pojo1 should not be injector");
        } catch (ConfigurationException ce)
        {
        	String nl = System.getProperty("line.separator");
        	assertThat(ce.getMessage()).isEqualTo("Guice configuration errors:"+nl
                +nl
                +"1) Explicit bindings are required and org.nuunframework.kernel.plugin.dummy4.Pojo1 is not explicitly bound."+nl
                + "  while locating org.nuunframework.kernel.plugin.dummy4.Pojo1"+nl
                +nl
                +"1 error"
             );
        }
        
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
    
    @Test
    public void plugin_sort_algo () throws Exception
    {
    	ArrayList<Plugin> plugins = new ArrayList<Plugin>() , plugins2 = null; 
    	
    	p1 e1 = new p1();
		plugins.add(e1);
    	p2 e2 = new p2();
		plugins.add(e2); // -> 13
    	p3 e3 = new p3();
		plugins.add(e3);
    	p4 e4 = new p4();
		plugins.add(e4);
    	p5 e5 = new p5();
		plugins.add(e5);
    	p6 e6 = new p6();
		plugins.add(e6);
    	p7 e7 = new p7();
		plugins.add(e7); // -> 11
    	p8 e8 = new p8();
		plugins.add(e8);
    	p9 e9 = new p9();
		plugins.add(e9);
    	p10 e10 = new p10();
		plugins.add(e10);
    	p11 e11 = new p11();
		plugins.add(e11);
    	p12 e12 = new p12();
		plugins.add(e12); // -> 6
    	p13 e13 = new p13();
		plugins.add(e13); // -> 11
    	
    	plugins2 = Whitebox.invokeMethod(underTest, "sortPlugins", plugins);
    	
    	assertThat(plugins2).isNotNull();    	
    	assertThat(plugins2).containsOnly ( e1 , e2 , e3 , e4, e5, e6, e7, e8, e9, e10, e11, e12, e13 );
    	assertThat(plugins2).containsSequence ( e11 , e13 , e6 , e12  , e10  );
    
    }
    
    
    static abstract class AbstractTestPlugin extends AbstractPlugin{
    	public Collection<Class<? extends Plugin>> dep(Class<? extends Plugin> klazz)
    	{
    		Collection<Class<? extends Plugin>> plugins = new ArrayList<Class<? extends Plugin>>();
            plugins.add(klazz);
            return plugins;
    	}
    }
    
    static class p1 extends AbstractTestPlugin
    {
    	public String name() {return this.getClass().getName(); }
    }
    static class p2 extends AbstractTestPlugin
    {
    	public String name() {return this.getClass().getName(); }    	
    	public Collection<Class<? extends Plugin>> requiredPlugins() {return dep(p13.class); }
    }
    static class p3 extends AbstractTestPlugin
    {
    	public String name() {return this.getClass().getName(); }
    }
    static class p4 extends AbstractTestPlugin
    {
    	public String name() {return this.getClass().getName(); }
    }
    static class p5 extends AbstractTestPlugin
    {
    	public String name() {return this.getClass().getName(); }
    }
    static class p6 extends AbstractTestPlugin
    {
    	public String name() {return this.getClass().getName(); }
    }
    static class p7 extends AbstractTestPlugin
    {
    	public String name() {return this.getClass().getName(); }
    	public Collection<Class<? extends Plugin>> requiredPlugins() {return dep(p11.class); }
    }
    static class p8 extends AbstractTestPlugin
    {
    	public String name() {return this.getClass().getName(); }
    }
    static class p9 extends AbstractTestPlugin
    {
    	public String name() {return this.getClass().getName(); }
    }
    static class p10 extends AbstractTestPlugin
    {
    	public String name() {return this.getClass().getName(); }
    }
    static class p11 extends AbstractTestPlugin
    {
    	public String name() {return this.getClass().getName(); }
    }
    static class p12 extends AbstractTestPlugin
    {
    	public String name() {return this.getClass().getName(); }
    	public Collection<Class<? extends Plugin>> requiredPlugins() {return dep(p6.class); }
    }    
    static class p13 extends AbstractTestPlugin
    {
    	public String name() {return this.getClass().getName(); }
    	public Collection<Class<? extends Plugin>> requiredPlugins() {return dep(p11.class); }
    }
    
    public Plugin createPlugin (final String name , final Class<? extends Plugin> dependency)
    {
    	return new AbstractPlugin () { 
			
			@Override
			public String name() {
				return name;
			}
			
			@Override
			public Collection<Class<? extends Plugin>> requiredPlugins() {
				
				if (dependency != null)
				{
					Set<Class <? extends Plugin>> plugins = new HashSet<Class<? extends Plugin>>();
					plugins.add(dependency);
					return plugins; 
				}
				else
				{
					return Collections.EMPTY_SET;
				}
				
			}
		};
    }
    
    @Test
    public void AliasMap_should_work()
    {
        AliasMap map = new AliasMap();
        map.putAlias("realkey1", "alias1");
        map.putAlias("realkey2", "alias2");
        
        Object object1 = new Object();        
        Object object2 = new Object();
        
        map.put("realkey1", object1.toString());
        map.put("realkey2", object2.toString());
        
        assertThat(map.get("alias1")).isEqualTo(object1.toString());
        assertThat(map.get("alias2")).isEqualTo(object2.toString());
        
        assertThat(map.containsKey("alias1")).isTrue();
        assertThat(map.containsKey("alias2")).isTrue();
    }

    @AfterClass
    public static void clear()
    {
        underTest.stop();
        end = System.currentTimeMillis();
        logger.info("Test took " + (end - start) + " ms.");
    }

}
