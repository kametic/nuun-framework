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
package org.nuunframework.kernel.stereotype;

import java.util.ArrayList;
import java.util.List;

import org.fest.assertions.Assertions;
import org.junit.AfterClass;
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
    
    public static class Plugin extends AbstractPlugin
    {
        
    	@Override
    	public String name ()
    	{
    		return "nominal plugin";
    	}

    	@Override
        public Object dependencyInjectionDef()
        {
            return new Module();
        }
    }
    
    public static class Module extends AbstractModule
    {
        
        @Override
        protected void configure()
        {
            bind(MyObj.class);
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
    
    @AfterClass
    public static void clear()
    {
        underTest.stop();
    }

}
