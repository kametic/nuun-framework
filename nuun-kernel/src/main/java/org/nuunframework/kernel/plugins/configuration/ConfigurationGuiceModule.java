package org.nuunframework.kernel.plugins.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import org.apache.commons.configuration.Configuration;

/**
 * 
 * @author 
 */
class ConfigurationGuiceModule extends AbstractModule
{

    private final Configuration configuration;

    public ConfigurationGuiceModule(Configuration configuration)
    {
        this.configuration = configuration;
    }

    @Override
    protected void configure()
    {
        bind(Configuration.class).toInstance(configuration);
        bindListener(Matchers.any(), new ConfigurationTypeListener(configuration));
    }
}
