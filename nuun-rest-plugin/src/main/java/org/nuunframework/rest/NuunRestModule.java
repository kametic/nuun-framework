package org.nuunframework.rest;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;


class NuunRestModule extends JerseyServletModule
{
    
    private final String uriPattern;
    private boolean enablePojoMappingFeature = true;
	private Class<? extends HttpServlet> jerseyCustomClass;

    public NuunRestModule(String uriPattern)
    {
        this.uriPattern = uriPattern;
    }

    public NuunRestModule(String uriPattern, boolean enablePojoMappingFeature, Class<? extends HttpServlet> jerseyCustomClass )
    {
        this(uriPattern);
        this.enablePojoMappingFeature = enablePojoMappingFeature;
		this.jerseyCustomClass = jerseyCustomClass;
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
        if (jerseyCustomClass == null) 
        {
        	serve(uriPattern).with(GuiceContainer.class,initParams);
        }
        else 
        {
        	bind(jerseyCustomClass);
        	serve(uriPattern).with(jerseyCustomClass , initParams);
        }
    }
}
