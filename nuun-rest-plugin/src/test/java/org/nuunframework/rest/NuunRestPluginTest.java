package org.nuunframework.rest;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nuunframework.kernel.Kernel;

public class NuunRestPluginTest
{

    static Kernel underTest;
	
	@BeforeClass
    public static void init()
    {
        underTest = Kernel.createKernel (
        		Kernel.NUUN_ROOT_PACKAGE , "org.nuunframework",
        		NuunRestPlugin.NUUN_JERSEY_GUICECONTAINER_CUSTOM_CLASS , SampleGuiceContainer.class.getName() ,
        		NuunRestPlugin.NUUN_REST_URL_PATTERN , "/rest/*"
        		).build();
        underTest.init();
        underTest.start();
    }
    
    @Test
	public void custom_guicecontainer_should_be_usable ()
	{
		assertThat(
		   underTest.getMainInjector().getInstance(SampleGuiceContainer.class)
		).isNotNull();
		assertThat(SampleGuiceContainer.initialized).isTrue();
	}

	
    @AfterClass
    public static void stop()
    {
    	underTest.stop();
    }
    
    void subresources_should_be_bound ()
    {
        
    }
}
