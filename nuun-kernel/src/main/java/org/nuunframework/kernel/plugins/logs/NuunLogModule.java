package org.nuunframework.kernel.plugins.logs;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

class NuunLogModule extends AbstractModule
{

    @Override
    protected void configure()
    {
        bindListener(Matchers.any(), new SLF4JTypeListener());
    }
}
