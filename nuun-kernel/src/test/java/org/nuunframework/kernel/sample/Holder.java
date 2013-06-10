package org.nuunframework.kernel.sample;

import org.nuunframework.kernel.plugins.logs.NuunLog;
import org.slf4j.Logger;

public class Holder
{
    @NuunLog
    private Logger myLogger;

    @AnnoCustomLog
    private Logger myLogger2;
    
    
    public Logger getLogger()
    {
        return myLogger;
    }

    public Logger getLogger2()
    {
        return myLogger2;
    }
}
