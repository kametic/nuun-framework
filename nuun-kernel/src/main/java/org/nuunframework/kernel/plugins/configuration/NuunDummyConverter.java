package org.nuunframework.kernel.plugins.configuration;


public class NuunDummyConverter implements NuunConfigurationConverter<String>
{

    @Override
    public String convert(String property)
    {
        return property;
    }

}
