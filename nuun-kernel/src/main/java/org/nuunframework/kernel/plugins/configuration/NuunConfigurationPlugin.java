package org.nuunframework.kernel.plugins.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.MapConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.nuunframework.kernel.context.InitContext;
import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.nuunframework.kernel.plugin.InitState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Module;

public class NuunConfigurationPlugin extends AbstractPlugin
{

    Logger logger = LoggerFactory.getLogger(NuunConfigurationPlugin.class);
    
    private Module module = null;

    private CompositeConfiguration configuration;
    private final List<Configuration> additionalConfigurations = new ArrayList<Configuration>();
    
    public NuunConfigurationPlugin()
    {
    }
    
    public void addConfiguration(Map<String, Object> configuration)
    {
    	additionalConfigurations.add(new MapConfiguration(configuration));
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
    public InitState init(InitContext initContext)
    {
     // find all properties classes in the classpath
        Collection<String> propertiesFiles = initContext.propertiesFiles();        

        configuration = new CompositeConfiguration();
        for (String propertiesFile : propertiesFiles)
        {
            logger.info("adding {} to module", propertiesFile);
            configuration.addConfiguration(configuration(propertiesFile));
        }
        for (Configuration additionalConfiguration : this.additionalConfigurations)
        {
        	logger.info("adding additionnal configuration {} to module", additionalConfiguration);
        	configuration.addConfiguration(additionalConfiguration);
        }
        return InitState.INITIALIZED;
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
