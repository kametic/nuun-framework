package org.nuunframework.kernel.plugins.configuration;

import java.util.Collection;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.nuunframework.kernel.context.InitContext;
import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Module;

public class NuunConfigurationPlugin extends AbstractPlugin
{

    Logger logger = LoggerFactory.getLogger(NuunConfigurationPlugin.class);
    
    
    private Module module = null;


    private CompositeConfiguration configuration;
    
    public NuunConfigurationPlugin()
    {
    }
    
    public Configuration getConfiguration()
    {
        return configuration;
    }

    @Override
    public String name()
    {
        return "nuun-configuration-plugin";
    }
    
    
    @Override
    public Module dependencyInjectionDef()
    {
        if (module == null )
        {
            module = new ConfigurationGuiceModule(configuration);
        }
        return module;
    }
    
    
    @Override
    public void init(InitContext initContext)
    {
     // find all properties classes in the classpath
        Collection<String> propertiesFiles = initContext.propertiesFiles();        

        configuration = new CompositeConfiguration();
        for (String propertiesFile : propertiesFiles)
        {
            logger.info("adding {} to module", propertiesFile);
            configuration.addConfiguration(configuration(propertiesFile));
        }
    }
    
    /**
     * reach properties file from classpath.
     * 
     * @param filePathInClasspath
     * @return return an Apache Configuration interface
     */
    private Configuration configuration(String filePathInClasspath)
    {
        try
        {
            return new PropertiesConfiguration(filePathInClasspath);
        }
        catch (ConfigurationException e)
        {
            e.printStackTrace();
            throw new IllegalStateException("Error in module initialization : Properties can not be initialized");
        }

    }
    
}
