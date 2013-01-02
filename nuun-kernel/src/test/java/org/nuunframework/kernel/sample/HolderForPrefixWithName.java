package org.nuunframework.kernel.sample;

import javax.inject.Inject;

import org.nuunframework.kernel.plugin.dummy1.BeanWithCustomSuffix;


public class HolderForPrefixWithName
{

    @Inject
    public BeanWithCustomSuffix customBean;
}