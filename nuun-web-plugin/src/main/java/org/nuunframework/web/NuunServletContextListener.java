package org.nuunframework.web;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.nuunframework.kernel.Kernel;
import org.reflections.util.ClasspathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class NuunServletContextListener extends GuiceServletContextListener
{

    private Logger         logger = LoggerFactory.getLogger(NuunServletContextListener.class);

    private ServletContext servletContext;
    private Kernel         kernel;

    public void contextInitialized(ServletContextEvent servletContextEvent)
    {
        // Needed in order to
        servletContext = servletContextEvent.getServletContext();
        super.contextInitialized(servletContextEvent);
    }

    @Override
    protected Injector getInjector()
    {
        // final String packageRoot = servletContext.getInitParameter("nuun.package.root");
        // final String uriPattern = servletContext.getInitParameter("nuun.uri.base.pattern");

        List<String> params = new ArrayList<String>();
        Enumeration<?> initparams = servletContext.getInitParameterNames();
        while (initparams.hasMoreElements())
        {
            String keyName = (String) initparams.nextElement();
            if (keyName != null && !keyName.isEmpty())
            {
                String value = servletContext.getInitParameter(keyName);
                logger.info("Adding {}={} to NuunServletContextListener.", keyName, value);
                params.add(keyName);
                params.add(value);
            }
        }

        Set<URL> webCPUrls = ClasspathHelper.forWebInfLib(servletContext);

        URL forWebInfClasses = ClasspathHelper.forWebInfClasses(servletContext);
        if (forWebInfClasses != null)
        {
            webCPUrls.add(forWebInfClasses);
        }

        logger.info("> "+webCPUrls + "<");
        
        int size = webCPUrls.size();
        if (webCPUrls != null && size > 0)
        {
            params.add(Kernel.NUUN_NUM_CP_PATH);
            params.add(Integer.toString(size));            
            int i = 0;
            for (URL url : webCPUrls)
            {
                params.add(Kernel.NUUN_CP_PATH_PREFIX + i);
                params.add(url.toString());
                logger.debug( Kernel.NUUN_CP_PATH_PREFIX + i + " = " +  url);
                i++;
            }
        }

        String[] paramsArray = new String[params.size()];
        params.toArray(paramsArray);

        kernel = Kernel.createKernel(paramsArray).build();
        // "nuun.package.root" , packageRoot , //
        // "nuun.uri.base.pattern" , uriPattern).build();

        kernel.init();

        kernel.start();

        return kernel.getMainInjector();
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent)
    {

        kernel.stop();

        // Injector injector = (Injector) servletContext.getAttribute(Injector.class.getName());

        super.contextDestroyed(servletContextEvent);
    }
}