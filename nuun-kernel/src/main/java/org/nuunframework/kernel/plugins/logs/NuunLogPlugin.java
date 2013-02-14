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
public class NuunLogPlugin extends AbstractPlugin
{
    
    private Module logModule = null;
    
    
    @Override
    public String name()
    {
        return "nuun-log-plugin";
    }
    
    @Override
    public Module dependencyInjectionDef()
    {
        if (logModule == null )
        {
            logModule = new NuunLogModule();
        }
        return logModule;
    }


}
