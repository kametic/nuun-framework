package org.nuunframework.kernel.api;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Collection;

import javax.inject.Inject;



import org.junit.Ignore;
import org.junit.Test;
import org.nuunframework.kernel.Kernel;
import org.nuunframework.kernel.api.topology.Instance;
import org.nuunframework.kernel.api.topology.InstanceMeta;
import org.nuunframework.kernel.api.topology.ObjectGraph;
import org.nuunframework.kernel.api.topology.ObjectGraphFactory;
import org.nuunframework.kernel.api.topology.Topology;
import org.nuunframework.kernel.plugin.Plugin;

import com.google.inject.Module;
public class AbstractObjectGraphDefTest
{

    static class MyObjectGraphDef extends Topology
    {
        @Override
        protected void describe()
        {
            String kernel = "kernel";
            String kernel2 = "kernel2";
            String plugin = "plugin";
            String module = "module";
            String inject = "inject";
            
            newInstance(kernel, Kernel.class);
            newInstance(kernel2, Kernel.class);
            newInstance(plugin , Plugin.class);
            newInstance(module , Module.class);
            newInstance(inject , Inject.class);
            newInstance("inherit" , Topology.class).asWildcard();
            
            newReference("refPlugin") .from(kernel) .to(plugin);
            newReference("refModule") .from(kernel) .to(module);
            newReference("refInject") .from(module) .to(inject).asOptionnal();
        }

        @Override
        public String name()
        {
            return "default";
        }
    }
    
    static class MyObjectGraphDefError1 extends Topology
    {
        @Override
        protected void describe()
        {
            String kernel = "kernel";
            String kernel2 = "kernel";
            
            newInstance(kernel, Kernel.class);
            newInstance(kernel2, Kernel.class);
            
        }

        @Override
        public String name()
        {
            return "error1";
        }
    }
    static class MyObjectGraphDefError2 extends Topology
    {
        @Override
        protected void describe()
        {
            String kernel = "kernel";
            String kernel2 = "kernel2";
            String plugin = "plugin";

            newInstance(kernel, Kernel.class);
            newInstance(kernel2, Kernel.class);
            newInstance(plugin , Plugin.class);
            
            newReference("kernel2") .from(kernel) .to(plugin);
        }

        @Override
        public String name()
        {
            return "error1";
        }
    }
    static class MyObjectGraphDefError3 extends Topology
    {
        @Override
        protected void describe()
        {
            String kernel = "kernel";
            String kernel2 = "kernel2";
            String plugin = "plugin";
            
            newInstance(kernel, Kernel.class);
            newInstance(kernel2, Kernel.class);
            newInstance(plugin , Plugin.class);
            
            newReference("kernel2") .from(kernel);
            newReference("kernel2") .from(kernel) .to(plugin);
        }

        @Override
        public String name()
        {
            return "error3";
        }
    }
    
    @Test
    public void object_graph_should_work()
    {
        ObjectGraphFactory objectGraphUnderTest = new MyObjectGraphDef();
        ObjectGraph generatedObjectGraph = objectGraphUnderTest.generate();
        
        assertThat(generatedObjectGraph).isNotNull();
        assertThat(generatedObjectGraph.instances()).hasSize(6);
        assertThat(generatedObjectGraph.references()).hasSize(3);
        
        assertThat(generatedObjectGraph.instancesByRegex(".ernel.*")).hasSize(2);
        
        Collection<Instance> instancesByClass = generatedObjectGraph.instancesByClass(Kernel.class);
        assertThat(instancesByClass).hasSize(2);
        for (Instance instance : instancesByClass)        
        {
            assertThat(instance.name()).startsWith("kernel");
        }
        assertThat(generatedObjectGraph.instance("kernel").references()).hasSize(2);
        assertThat(generatedObjectGraph.instance("kernel2").references()).hasSize(0);

        assertThat(generatedObjectGraph.instance("inherit").meta()).isEqualTo(InstanceMeta.WILDCARD);

        assertThat(generatedObjectGraph.reference("refInject").optionnal()).isTrue(); 
        
        assertThat(generatedObjectGraph.instancesAssignableFrom(ObjectGraphFactory.class)).hasSize(1);
    }
    
    @Test(expected=IllegalStateException.class)
    public void object_graphdef_should_raise_exception_when_two_instances_with_same_name ()
    {
        ObjectGraphFactory objectGraphUnderTest = new MyObjectGraphDefError1();
        ObjectGraph generatedObjectGraph = objectGraphUnderTest.generate();
    }

    @Ignore
    @Test(expected=IllegalStateException.class)
    public void object_graphdef_should_raise_exception_when_two_instance_and_reference_with_same_name ()
    {
        ObjectGraphFactory objectGraphUnderTest = new MyObjectGraphDefError2();
        ObjectGraph generatedObjectGraph = objectGraphUnderTest.generate();
    }
    @Test(expected=IllegalStateException.class)
    public void object_graphdef_should_raise_exception_when_dsl_badly_used ()
    {
        ObjectGraphFactory objectGraphUnderTest = new MyObjectGraphDefError3();
        ObjectGraph generatedObjectGraph = objectGraphUnderTest.generate();
    }

}
