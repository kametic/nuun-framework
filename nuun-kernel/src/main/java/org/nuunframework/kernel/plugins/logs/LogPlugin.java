/**
 * 
 */
package org.nuunframework.kernel.plugins.logs;

import org.nuunframework.kernel.plugin.AbstractPlugin;

import com.google.inject.Module;


/**
 * @author Epo Jemba
 *
 */
public class LogPlugin extends AbstractPlugin
{
    
    private Module logModule = null;
    
    
    @Override
    public String name()
    {
        return "LogPlugin";
    }
    
    @Override
    public Module dependencyInjectionDef()
    {
        if (logModule == null )
        {
            logModule = new LogModule();
        }
        return logModule;
    }


}
