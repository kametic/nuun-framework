package org.nuunframework.kernel.sample;

import javax.inject.Inject;

import org.nuunframework.kernel.plugin.dummy1.Bean6;
import org.nuunframework.kernel.plugin.dummy1.Bean9;
import org.nuunframework.kernel.plugin.dummy1.DummyService;
import org.nuunframework.kernel.plugins.configuration.Property;


public class HolderForPlugin
{

    @Property("value1")
    public String       value;

    @Inject
    public DummyService dummyService;

    @Inject
    public Bean6        bean6;
    
    @Inject
    public Bean9        bean9;
}