package org.nuunframework.web;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.nuunframework.kernel.Kernel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class NuunServletContextListener extends GuiceServletContextListener
{

    private Logger logger = LoggerFactory.getLogger(NuunServletContextListener.class);
    
    private ServletContext servletContext;
    private Kernel kernel;

    public void contextInitialized(ServletContextEvent servletContextEvent)
    {
        // Needed in order to
        servletContext = servletContextEvent.getServletContext();
        super.contextInitialized(servletContextEvent);
    }

    @Override
    protected Injector getInjector()
    {
//      final String packageRoot = servletContext.getInitParameter("sts.package.root");  
//      final String uriPattern  = servletContext.getInitParameter("sts.uri.base.pattern");  
        
        List<String> params = new ArrayList<String>();
        Enumeration<?> initparams = servletContext.getInitParameterNames(); 
        while(   initparams.hasMoreElements() ) 
        {
            String keyName = (String) initparams.nextElement();
            if (keyName != null && ! keyName.isEmpty())
            {
                String value = servletContext.getInitParameter(keyName);
                logger.info("Adding {}={} to NuunServletContextListener." , keyName , value );
                params.add(keyName);
                params.add(value);
            }
        }

        String [] paramsArray = new String[params.size()];        
        params.toArray(paramsArray);
        
        kernel = Kernel.createKernel(   paramsArray).build ();
//            "sts.package.root" , packageRoot ,   //
//            "sts.uri.base.pattern" , uriPattern).build();
        
        kernel.init();
        
        kernel.start();
        
        return kernel.getMainInjector();
    }
    
    public void contextDestroyed(ServletContextEvent servletContextEvent)
    {
        
        kernel.stop();
        
//        Injector injector = (Injector) servletContext.getAttribute(Injector.class.getName());
        
        super.contextDestroyed(servletContextEvent);
    }
}