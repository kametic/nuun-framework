package org.nuunframework.kernel;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.After;
import org.junit.Test;
import org.nuunframework.kernel.plugin.dummy7.DummyPlugin7_A;
import org.nuunframework.kernel.plugin.dummy7.DummyPlugin7_B;

import com.google.inject.Key;
import com.google.inject.name.Names;

public class KernelSuite7Test
{

    private Kernel underTest;
    
    @Test
    public void dependee_plugins_that_misses_should_be_source_of_error()
    {
        underTest = Kernel.createKernel() //
                .withoutSpiPluginsLoader() //
                .withPlugins( //
                        new DummyPlugin7_A(), //
                        new DummyPlugin7_B() //
                ) //
                .build(); //
        underTest.init();
        underTest.start();
        
        String resa = underTest.getMainInjector().getInstance( Key.get(String.class, Names.named("dep7a")) );
        assertThat(resa).isNotNull();
        assertThat(resa).isEqualTo("dep7aOVER");
    }
    
    @Test
    public void dependee_plugins_that_misses_should_be_source_of_error_()
    {
        underTest = Kernel.createKernel() //
                .withoutSpiPluginsLoader() //
                .withPlugins( //
                        new DummyPlugin7_A(), //
                        new DummyPlugin7_B() //
                        ) //
                        .build(); //
        underTest.init();
        underTest.start();
        
        String resa = underTest.getMainInjector().getInstance( Key.get(String.class, Names.named("dep7b")) );
        assertThat(resa).isNotNull();
        assertThat(resa).isEqualTo("dep7bOVER");
        
    }


    @After
    public void stopKernel()
    {
        underTest.stop();
    }

}
