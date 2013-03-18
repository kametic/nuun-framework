package org.nuunframework.kernel.plugin;

import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.nuunframework.kernel.context.Context;
import org.nuunframework.kernel.context.InitContext;
import org.nuunframework.kernel.plugin.provider.DependencyInjectionProvider;
import org.nuunframework.kernel.plugin.request.BindingRequest;
import org.nuunframework.kernel.plugin.request.ClasspathScanRequest;
import org.nuunframework.kernel.plugin.request.KernelParamsRequest;

import com.google.inject.Module;

/**
 * @author Epo Jemba
 */
public interface Plugin
{
    /**
     * Lifecycle method : init()
     */
    void init(InitContext initContext);

    /**
     * Lifecycle method : start()
     */
    void start(Context context);

    /**
     * Lifecycle method : stop()
     */
    void stop();

    /**
     * Lifecycle method : destroy()
     */
    void destroy();

    /**
     * The name of the plugin. Plugin won't be installed, if there is no a name. And if this name is not
     * unique. Mandatory
     * 
     * @return the plugin name.
     */
    String name();

    /**
     * The description of the plugin.
     * 
     * @return
     */
    String description();

    /* ============================= PLUGIN info and requests ============================= * */

    /**
     * list of kernel params needed by this plugins required by this plugin
     * 
     * @return
     */
    Collection<KernelParamsRequest> kernelParamsRequests();

    /**
     * List of classpath request needed by
     * 
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
     * list of plugins dependencies required by this plugin
     * 
     * @return
     */
    Collection<Class<? extends Plugin>> pluginDependenciesRequired();

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
     * Return an object that will contains the dependency injection definitions. Mostly a {@link Module} but
     * it can be other dependency injection object from other ioc frameworks : Spring, Tapestry, Jodd, Dagger.
     * The kernel must have a {@link DependencyInjectionProvider} that handle it.
     * 
     * @return
     */
    Object dependencyInjectionDef();

    /**
     * The kernel allow the plugin to compute additionnal classpath to scan. He passes a containerContext that
     * may be a ServletContext for servlet environement, BundleContext for osgi environnement or something
     * else.
     * 
     * @param containerContext
     * @return
     */
    Set<URL> computeAdditionalClasspathScan(Object containerContext);

    /**
     * return a dependency injection provider to the kernel.
     * 
     * @return a DependencyInjectionProvider
     */
    DependencyInjectionProvider dependencyInjectionProvider();

    /**
     * Re
     * 
     * return a Map which contains
     * <pre> 
     * - key :the alias to create. 
     * - value :  kernel parameter to alias.
     * </pre>
     */
    Map<String, String> kernelParametersAliases();

}
