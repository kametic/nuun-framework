package org.nuunframework.ensemble.engine;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class EnsembleModule extends AbstractModule
{
    private final Context context;

    public EnsembleModule(Context context)
    {
        this.context = context;
    }
    
    @Override
    protected void configure()
    {
        bindInterceptor(Matchers.any(), Matchers.any(), new EnsembleRecorder(context));
    }
}


