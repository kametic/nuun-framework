package org.nuunframework.kernel.stereotype;

import java.util.ArrayList;
import java.util.List;

import org.fest.assertions.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nuunframework.kernel.Kernel;
import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.nuunframework.kernel.stereotype.sample.CachePlugin;
import org.nuunframework.kernel.stereotype.sample.LogPlugin;
import org.nuunframework.kernel.stereotype.sample.SecurityPlugin;

import com.google.inject.AbstractModule;

public class ConcernTest
{

    static Kernel underTest;
    private static List<String> list;
    
    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void init()
    {
        list = new ArrayList<String>();
        underTest = Kernel.createKernel().withoutSpiPluginsLoader().withPlugins(new Plugin() , new CachePlugin(list ) , new LogPlugin(list) , new SecurityPlugin(list)).build();
        underTest.init();
        underTest.start();
    }
    
    static class MyObj
    {
        
        void triggerMethod(List<String> list)
        {
            list.add("fire");
        }
    }
    
    static class Plugin extends AbstractPlugin
    {
        @Override
        public String name()
        {
            return "nominal module";
        }
        
        @Override
        public Object dependencyInjectionDef()
        {
            return new AbstractModule()
            {
                
                @Override
                protected void configure()
                {
                    bind(MyObj.class);
                }
            };
        }
    }
    
    @Test
    public void test()
    {
        
        MyObj obj = underTest.getMainInjector().getInstance(MyObj.class);
        obj.triggerMethod(list);
        Assertions.assertThat(list).hasSize(7);
        Assertions.assertThat(list).containsExactly("pre security" , "pre cache" , "pre log", "fire" , "post log",  "post cache"  ,  "post security");
    }

}
