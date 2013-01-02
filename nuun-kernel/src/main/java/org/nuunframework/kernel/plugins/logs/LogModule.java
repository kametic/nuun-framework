package org.nuunframework.kernel.plugins.logs;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class LogModule extends AbstractModule
{

    @Override
    protected void configure()
    {
        bindListener(Matchers.any(), new SLF4JTypeListener());
    }
}
