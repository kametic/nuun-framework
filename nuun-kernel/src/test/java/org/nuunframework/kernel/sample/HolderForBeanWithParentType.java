package org.nuunframework.kernel.sample;

import javax.inject.Inject;

import org.nuunframework.kernel.plugin.dummy1.BeanWithParentType;


public class HolderForBeanWithParentType
{

    @Inject
    public BeanWithParentType beanWithParentType;
}
