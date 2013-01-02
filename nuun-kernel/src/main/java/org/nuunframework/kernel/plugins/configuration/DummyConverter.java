package org.nuunframework.kernel.plugins.configuration;


public class DummyConverter implements ConfigurationConverter<String>
{

    @Override
    public String convert(String property)
    {
        return property;
    }

}
