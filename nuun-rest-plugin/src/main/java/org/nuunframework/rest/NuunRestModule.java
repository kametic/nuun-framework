package org.nuunframework.rest;

import java.util.HashMap;
import java.util.Map;

import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;


public class NuunRestModule extends JerseyServletModule
{
    
    private final String uriPattern;
    private boolean enablePojoMappingFeature = true;

    public NuunRestModule(String uriPattern)
    {
        this.uriPattern = uriPattern;
    }

    public NuunRestModule(String uriPattern, boolean enablePojoMappingFeature )
    {
        this(uriPattern);
        this.enablePojoMappingFeature = enablePojoMappingFeature;
    }
    
    @Override
    protected void configureServlets()
    {
        Map<String, String> initParams = new HashMap<String, String>();
        
        if (enablePojoMappingFeature)
        {
            initParams.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
        }
        else 
        {
            initParams.put("com.sun.jersey.api.json.POJOMappingFeature", "false");
        }
        bind(GuiceContainer.class);
        serve(uriPattern).with(GuiceContainer.class,initParams);
    }
}
