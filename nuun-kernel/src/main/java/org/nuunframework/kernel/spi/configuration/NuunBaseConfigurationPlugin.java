package org.nuunframework.kernel.spi.configuration;

import java.util.Map;

public interface NuunBaseConfigurationPlugin
{
    void addConfiguration(Map<String, Object> configuration);
    
    
    Object getConfiguration();
}
