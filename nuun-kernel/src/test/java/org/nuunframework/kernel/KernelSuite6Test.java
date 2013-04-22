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
package org.nuunframework.kernel;

import static org.fest.assertions.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Test;
import org.nuunframework.kernel.plugin.Plugin;
import org.nuunframework.kernel.plugin.dummy6.DummyPlugin6_A;
import org.nuunframework.kernel.plugin.dummy6.DummyPlugin6_B;
import org.nuunframework.kernel.plugin.dummy6.DummyPlugin6_C;
import org.nuunframework.kernel.plugin.dummy6.DummyPlugin6_D;
import org.nuunframework.kernel.plugin.dummy6.T2;
import org.powermock.reflect.Whitebox;

public class KernelSuite6Test
{

    private Kernel underTest;

    @Test
    public void dependee_plugins_that_misses_should_be_source_of_error()
    {
        try
        {
            underTest = Kernel.createKernel() //
                    .withoutSpiPluginsLoader() //
                    .withPlugins( //
                            new DummyPlugin6_A(), //
                            new DummyPlugin6_B() //
                            /** ,* new* DummyPlugin6_C) */
                            , //
                            new DummyPlugin6_D()) //
                    .build(); //
            underTest.init();
            assertThat(true).isFalse();

        }
        catch (KernelException ke)
        {
            underTest.stop();
            assertThat(ke.getMessage())
                    .isEqualTo(
                            "plugin dummy-plugin-6-B misses the following plugin/s as dependee/s [class org.nuunframework.kernel.plugin.dummy6.DummyPlugin6_D, class org.nuunframework.kernel.plugin.dummy6.DummyPlugin6_C]");
        }
    }

    @Test
    public void dependee_plugins_should_start()
    {

        DummyPlugin6_C dummyPlugin6_C = new DummyPlugin6_C();
        DummyPlugin6_D dummyPlugin6_D = new DummyPlugin6_D();
        underTest = Kernel.createKernel().withoutSpiPluginsLoader().withPlugins(new DummyPlugin6_A(), new DummyPlugin6_B(), dummyPlugin6_C, dummyPlugin6_D).build();
        assertThat(dummyPlugin6_C.isInternal()).isFalse();
        assertThat(dummyPlugin6_D.isInternal()).isFalse();
        underTest.init();
        // Plugin B has initialized the C and D 
        assertThat(dummyPlugin6_C.isInternal()).isTrue();
        assertThat(dummyPlugin6_D.isInternal()).isTrue();
        underTest.start();
        
        T2 instance = underTest.getMainInjector().getInstance(T2.class);
        assertThat(instance).isNotNull();

    }

    @Test
    public void plugin_sort_algo() throws Exception
    {
        underTest = Kernel.createKernel().withoutSpiPluginsLoader().build();
        underTest.init();
        underTest.start();
        
        
        ArrayList<Plugin> plugins = new ArrayList<Plugin>(), plugins2 = null;

//        DummyPlugin6_A a = new DummyPlugin6_A();
//        plugins.add(a);
        DummyPlugin6_C c = new DummyPlugin6_C();
        plugins.add(c);
        DummyPlugin6_D d = new DummyPlugin6_D();
        plugins.add(d);
        DummyPlugin6_B b = new DummyPlugin6_B();  // <--- C ,  D
        plugins.add(b); 

        plugins2 = Whitebox.invokeMethod(underTest, "initRoundEnvironment");
        plugins2 = Whitebox.invokeMethod(underTest, "sortPlugins", plugins);

        assertThat(plugins2).isNotNull();
        assertThat(plugins2).containsOnly(b, c, d);
        assertThat(plugins2).containsSequence(b, d, c );
        
        
    }
    
    @After
    public void stopKernel()
    {
        underTest.stop();
    }

}
