/**
 * Copyright (C) 2013 Kametic <epo.jemba@kametic.com>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * or any later version
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    InitState init(InitContext initContext);

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
    Collection<Class<? extends Plugin>> requiredPlugins();

    /**
     * list of plugins that become dependent on "this" plugin.
     * Returns 
     * 
     *  If Z. return {A.class} A.will become dependent on Z. 
     *  Z will virtually need A. Even if Z did not ask for it.
     *  this allow pre init between plugin.
     *  
     *  InitContext?
     * 
     * 
     * @return
     */
    Collection<Class<? extends Plugin>> dependentPlugins();

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
     * This object will contains bindings definition that will override the main dependency ones.
     * It can serve as test bindings replacement, environment definition. 
     * Those can be also handled by a {@link DependencyInjectionProvider} if keys match.
     * 
     * @return
     */
    Object dependencyInjectionOverridingDef();

    /**
     * Practical method to retrieve the container context as it is passed as argument.
     * @param containerContext the context of the container
     */
    void provideContainerContext(Object containerContext);
    
    /**
     * The kernel allow the plugin to compute additionnal classpath to scan. Method can use the containerContext - that
     * may be a ServletContext for servlet environement, BundleContext for osgi environnement or something
     * else - given to the plugin via method provideContainerContext.
     * 
     * @return
     */
    Set<URL> computeAdditionalClasspathScan();

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
    
    /**
     * Round Environment provide information regarding the current round to the plugin.
     * The kernel pass this object to the plugin before all 
     * 
     * @param roundEnvironment
     */
    void provideRoundEnvironment (RoundEnvironment roundEnvironment);

}
