package org.nuunframework.web;

import java.net.URL;
import java.util.Set;

import javax.servlet.ServletContext;

import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.reflections.util.ClasspathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.servlet.WorkAroundModule;

public class NuunWebPlugin extends AbstractPlugin
{

    private Logger         logger = LoggerFactory.getLogger(NuunWebPlugin.class);
    
    private WorkAroundModule module = null;
    private Set<URL> additionalClasspath = null;

    @Override
    public String name()
    {
        return "nuun-web-plugin";
    }

    @Override
    public Set<URL> computeAdditionalClasspathScan()
    {
        ServletContext servletContext = null;

        if (containerContext != null && ServletContext.class.isAssignableFrom(containerContext.getClass()))
        {

            servletContext = (ServletContext) containerContext;
            
            Set<URL> webCPUrls = ClasspathHelper.forWebInfLib(servletContext);
            URL forWebInfClasses = ClasspathHelper.forWebInfClasses(servletContext);
            webCPUrls.add(forWebInfClasses);
            
          this.additionalClasspath = webCPUrls;
            
        }   
        
        return  this.additionalClasspath != null ? this.additionalClasspath : super.computeAdditionalClasspathScan() ;
    }
}
