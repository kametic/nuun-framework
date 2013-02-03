package org.nuunframework.kernel.plugin;

import java.net.URL;
import java.util.Collection;
import java.util.Set;

import org.nuunframework.kernel.context.Context;
import org.nuunframework.kernel.context.InitContext;
import org.nuunframework.kernel.plugin.request.BindingRequest;
import org.nuunframework.kernel.plugin.request.ClasspathScanRequest;
import org.nuunframework.kernel.plugin.request.KernelParamsRequest;


/**
 * 
 * 
 * 
 * @author Epo Jemba
 *
 */
public interface Plugin
{
    /**
     * Lifecycle method : init()
     * 
     */
    void init(InitContext initContext);
    
    /**
     * Lifecycle method : start()
     * 
     */
    void start(Context context);

    /**
     * Lifecycle method : stop()
     * 
     */
    void stop();
    
    /**
     * Lifecycle method : destroy()
     * 
     */
    void destroy();
    
    
    /**
     * The name of the plugin. Plugin won't be installed, if there is no a name. And if this name is not unique.
     * Mandatory 
     * @return the plugin name.
     */
    String name ();

    /**
     * The description of the plugin. 
     * @return
     */
    String description ();
    
    /*   ============================= PLUGIN info and requests =============================  **/
    
    /**
     * 
     * list of kernel params needed by this plugins required by this plugin
     * @return
     */
    Collection<KernelParamsRequest> kernelParamsRequests();

    /**
     * 
     * List of classpath request needed by 
     * @return
     */
    Collection<ClasspathScanRequest> classpathScanRequests();
    
    /**
     * List of bind request
     * 
     * @return
     */
    Collection<BindingRequest> bindingRequests();
    
    /**
     * 
     * list of plugins dependencies required by this plugin
     * @return
     */
    Collection< Class<? extends Plugin>> pluginDependenciesRequired();
    
    
    /**
     * The prefix for all the properties for this plugin.
     * 
     * @return
     */
    String pluginPropertiesPrefix();
    
    /**
     * The package root from where the nuun core will scan for annotation 
     * 
     * @return
     */
    String pluginPackageRoot();
    
    /**
     *  
     * @return 
     */
    Object dependencyInjectionDef();
    
    
    Set<URL> computeAdditionalClasspathScan(Object containerContext);
    
    
    
}
