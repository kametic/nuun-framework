package org.nuunframework.kernel.sample;

import javax.inject.Inject;

import org.nuunframework.kernel.context.Context;


public class HolderForContext
{

    public final Context context;

    @Inject
    public HolderForContext(Context context)
    {
        this.context = context;
    }
}