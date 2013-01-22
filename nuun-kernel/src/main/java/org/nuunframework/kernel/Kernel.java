/**
 * 
 */
package org.nuunframework.kernel;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import org.apache.commons.collections.iterators.ArrayIterator;
import org.nuunframework.kernel.context.Context;
import org.nuunframework.kernel.context.RequestContextInternal;
import org.nuunframework.kernel.internal.InternalKernelGuiceModule;
import org.nuunframework.kernel.plugin.Plugin;
import org.nuunframework.kernel.plugin.request.BindingRequest;
import org.nuunframework.kernel.plugin.request.ClasspathScanRequest;
import org.nuunframework.kernel.plugin.request.KernelParamsRequest;
import org.nuunframework.kernel.plugin.request.KernelParamsRequestType;
import org.nuunframework.kernel.spi.DependencyInjectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author Epo Jemba
 */
public final class Kernel
{

    public static final String          NUUN_ROOT_PACKAGE      = "nuun.root.package";
    public static final String          NUUN_NUM_CP_PATH      = "nuun.num.classpath.path";
    public static final String          NUUN_CP_PATH_PREFIX      = "nuun.classpath.path.prefix-";
    private Logger                      logger                = LoggerFactory.getLogger(Kernel.class);
    private static Kernel               kernel;

    private final String                NUUN_PROPERTIES_PREFIX = "nuun-";

    private ServiceLoader<Plugin>       pluginLoader;
    private Map<String, Plugin>         plugins               = Collections.synchronizedMap(new HashMap<String, Plugin>()); //
    private Map<String, Plugin>         pluginsToAdd          = Collections.synchronizedMap(new HashMap<String, Plugin>()); //

    private final RequestContextInternal currentContext;
    private Injector                    mainInjector;
    private final Map<String, String>   kernelParams;

    private Kernel(String... keyValues)
    {

        kernelParams = new HashMap<String, String>();

        this.currentContext = new RequestContextInternal(NUUN_PROPERTIES_PREFIX, kernelParams);

        @SuppressWarnings("unchecked")
        Iterator<String> it = new ArrayIterator(keyValues);
        while (it.hasNext())
        {
            String key = it.next();
            String value = "";
            if (it.hasNext())
                value = it.next();
            logger.info("Adding {} = {} as param to kernel", key, value   );
            kernelParams.put(key, value);
        }

        //
        if (kernelParams.containsKey(NUUN_ROOT_PACKAGE))
        {
            String tmp = kernelParams.get(NUUN_ROOT_PACKAGE);

            String[] packages = null;
            if (tmp != null)
            {
                packages = tmp.split(",");

                for (String pack : packages)
                {
                    logger.info("Adding {} as nuun.package.root", pack);
                    this.currentContext.addPackageRoot(pack);
                }
            }
        }
    }

    private boolean                                 started     = false;
    private boolean                                 initialized = false;
    private Context                                 context;
    private Collection<DependencyInjectionProvider> dependencyInjectionProviders;
    
    public boolean isStarted ()
    {
        return started;
    }
    
    public boolean isInitialized()
    {
        return initialized;
    }
    
    /**
     * 
     * 
     */
    public synchronized void init()
    {
        if (!initialized)
        {
            checkPlugins();
            initPlugins();
            initialized = true;
        }
        else
        {
            throw new KernelException("Kernel is already initialized");
        }
    }

    public synchronized void start()
    {
        if (initialized)
        {
            // All bindings will be computed
            mainInjector = Guice.createInjector(new InternalKernelGuiceModule(currentContext));
            context = mainInjector.getInstance(Context.class);

            // 1) inject plugins via injector
            // 2) inject context via injecto
            // 2) start them

            for (Plugin plugin : plugins.values())
            {
                mainInjector.injectMembers(plugin);

                plugin.start(context);
            }

            started = true;
        }
        else
        {
            throw new KernelException("Kernel is not initialized.");
        }
    }

    public Injector getMainInjector()
    {
        return mainInjector;
    }

    public void stop()
    {
        // 1) stop plugins
        //
        for (Plugin plugin : plugins.values())
        {
            plugin.stop();
        }

        // remove singleton reference
        kernel = null;

    }

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    private void checkPlugins()
    {
        logger.info("Plugins initialisation");
        plugins.clear();
        pluginLoader = ServiceLoader.load(Plugin.class, Thread.currentThread().getContextClassLoader());

        // plugin from service loader
        Iterator<Plugin> iterator1 = pluginLoader.iterator();

        // plugin from kernel call api
        Iterator<Plugin> iterator2 = pluginsToAdd.values().iterator();

        List<Iterator<Plugin>> iterators = Arrays.asList(iterator1, iterator2);

        List<Class<? extends Plugin>> pluginClasses = new ArrayList<Class<? extends Plugin>>();
        // we initialize plugins
        for (Iterator<Plugin> iterator : iterators)
        {
            Plugin plugin;
            while (iterator.hasNext())
            {
                plugin = iterator.next();
                logger.info("checking Plugin {}." , plugin.name());
                if (!Strings.isNullOrEmpty(plugin.name()))
                {
                    Object ok = plugins.put(plugin.name(), plugin);
                    if (ok == null)
                    {
                        // Check for required param
                        // ========================
                        Collection<KernelParamsRequest> kernelParamsRequests = plugin.kernelParamsRequests();
                        Collection<String> computedMandatoryParams = new HashSet<String>();
                        for (KernelParamsRequest kernelParamsRequest : kernelParamsRequests)
                        {
                            if (kernelParamsRequest.requestType == KernelParamsRequestType.MANDATORY)
                            {
                                computedMandatoryParams.add(kernelParamsRequest.keyRequested);
                            }
                        }

                        if (kernelParams.keySet().containsAll(computedMandatoryParams))
                        {
                            pluginClasses.add(plugin.getClass());
                        }
                        else
                        {
                            logger.error("plugin {} miss parameter/s : {}", plugin.name(), plugin.kernelParamsRequests().toString());
                            throw new KernelException("plugin " + plugin.name() + " miss parameter/s : " + plugin.kernelParamsRequests().toString());
                        }

                    }
                    else
                    {
                        logger.error("Plugin {} is already in the installed list. please fix this before the kernel can start.", plugin.name());
                        throw new KernelException("Plugin %s is already in the installed list. please fix this before the kernel can start.", plugin.name());

                    }
                }
                else
                {
                    logger.warn("Plugin {} has no correct name it won't be installed.", plugin.getClass());
                    throw new KernelException("Plugin %s has no correct name it won't be installed.", plugin.name());
                }
            }
        }

        // Check for dependencies
        for (Plugin plugin : plugins.values())
        {
            if (!plugin.pluginDependenciesRequired().isEmpty() && !pluginClasses.containsAll(plugin.pluginDependenciesRequired()))
            {
                logger.error("plugin {} misses the following plugin/s as dependency/ies {}", plugin.name(), plugin.pluginDependenciesRequired().toString());
                throw new KernelException("plugin %s misses the following plugin/s as dependency/ies %s", plugin.name(), plugin.pluginDependenciesRequired()
                        .toString());

            }
        }
    }

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    private void initPlugins()
    {
        //
        for (Plugin plugin : plugins.values())
        {
            // Configure properties prefixes
            if (!Strings.isNullOrEmpty(plugin.pluginPropertiesPrefix()))
            {
                this.currentContext.addPropertiesPrefix(plugin.pluginPropertiesPrefix());
            }

            // Configure package root
            if (!Strings.isNullOrEmpty(plugin.pluginPackageRoot()))
            {
                this.currentContext.addPackageRoot(plugin.pluginPackageRoot());
            }

            if (plugin.classpathScanRequests() != null && plugin.classpathScanRequests().size() > 0)
            {
                for (ClasspathScanRequest request : plugin.classpathScanRequests())
                {
                    switch (request.requestType)
                    {
                        case ANNOTATION_TYPE:
                            this.currentContext.addAnnotationTypesToScan((Class<? extends Annotation>) request.objectRequested);
                            break;
                        case ANNOTATION_REGEX_MATCH:
                            this.currentContext.addAnnotationRegexesToScan((String) request.objectRequested);
                            break;
                        case SUBTYPE_OF_BY_CLASS:
                            this.currentContext.addParentTypeClassToScan((Class<?>) request.objectRequested);
                            break;
                        case SUBTYPE_OF_BY_REGEX_MATCH:
                            this.currentContext.addParentTypeRegexesToScan((String) request.objectRequested);
                            break;
                        // case TYPE_OF_BY_CLASS:
                        // this.currentContext.addTypeClassToScan((Class<?>) request.objectRequested);
                        // break;
                        case TYPE_OF_BY_REGEX_MATCH:
                            this.currentContext.addTypeRegexesToScan((String) request.objectRequested);
                            break;
                        default:
                            logger.warn("{} is not a ClasspathScanRequestType a o_O", request.requestType);
                            break;
                    }
                }
            }

            if (plugin.bindingRequests() != null && plugin.bindingRequests().size() > 0)
            {
                for (BindingRequest request : plugin.bindingRequests())
                {
                    switch (request.requestType)
                    {
                        case ANNOTATION_TYPE:
                            this.currentContext.addAnnotationTypesToBind((Class<? extends Annotation>) request.objectRequested);
                            break;
                        case ANNOTATION_REGEX_MATCH:
                            this.currentContext.addAnnotationRegexesToBind((String) request.objectRequested);
                            break;
                        case SUBTYPE_OF_BY_CLASS:
                            this.currentContext.addParentTypeClassToBind((Class<?>) request.objectRequested);
                            break;
                        case SUBTYPE_OF_BY_REGEX_MATCH:
                            this.currentContext.addTypeRegexesToBind((String) request.objectRequested);
                            break;
                        default:
                            logger.warn("{} is not a BindingRequestType a o_O", request.requestType);
                            break;
                    }
                }
            }
        }

        // We also want to scan DependencyInjectionProvider before the classpath scan
        currentContext.addParentTypeClassToScan(DependencyInjectionProvider.class);

        // We launch classpath scan and store results of requests
        this.currentContext.executeRequests();

        // Check for dependencies
        for (Plugin plugin : plugins.values())
        {
            logger.info("initializing Plugin {}." , plugin.name());
            plugin.init(currentContext);
        }

        // Convert dependency manager classes to instances
        Collection<Class<?>> dependencyInjectionProvidersClasses = currentContext.scannedSubTypesByParentClass().get(DependencyInjectionProvider.class);
        createDependencyInjectionProvidersMap(dependencyInjectionProvidersClasses);

        for (Plugin plugin : plugins.values())
        {
            // Configure module
            if (plugin.dependencyInjectionDef() != null)
            {
                if (plugin.dependencyInjectionDef() instanceof com.google.inject.Module)
                {
                    this.currentContext.addChildModule(com.google.inject.Module.class.cast(plugin.dependencyInjectionDef()));
                }
                else
                {
                    DependencyInjectionProvider provider = null;
                    providerLoop: for (DependencyInjectionProvider providerIt : dependencyInjectionProviders)
                    {
                        if (providerIt.canHandle(plugin.dependencyInjectionDef().getClass()))
                        {
                            provider = providerIt;
                            break providerLoop;
                        }
                    }
                    if (provider != null)
                    {
                        this.currentContext.addChildModule(provider.convert(plugin.dependencyInjectionDef()));
                    }
                    else
                    {
                        logger.error("Kernel did not recognize module {} of plugin {}", plugin.dependencyInjectionDef(), plugin.name());
                        throw new KernelException("Kernel did not recognize module %s of plugin %s. Please provide a DependencyInjectionProvider.", plugin.dependencyInjectionDef().toString(), plugin.name());
                    }
                }
            }
        }

    }

    void createDependencyInjectionProvidersMap(Collection<Class<?>> dependencyInjectionProvidersClasses)
    {
//        Map<Class<?>, DependencyInjectionProvider> map = new HashMap<Class<?>, DependencyInjectionProvider>();

        dependencyInjectionProviders = new HashSet<DependencyInjectionProvider>();

        for (Class<?> dependencyInjectionProviderClass : dependencyInjectionProvidersClasses)
        {
            DependencyInjectionProvider injectionDependencyProvider = newInstance(dependencyInjectionProviderClass);
            if (injectionDependencyProvider != null)
            {
                dependencyInjectionProviders.add(injectionDependencyProvider);
            }
            else
            {
                throw new KernelException("DependencyInjectionProvider %s can not be instanciated", (Object) dependencyInjectionProviderClass);
            }
        }
    }

    /**
     * You have only one chance to get the current kernel.
     * 
     * @param keyValues
     * @return
     */
    public synchronized static KernelBuilderWithPlugin createKernel(String... keyValues)
    {
        return new KernelBuilderImpl(keyValues);
    }

    public static interface KernelBuilder
    {

        Kernel build();
    }

    public static interface KernelBuilderWithPlugin extends KernelBuilder
    {

        KernelBuilder withPlugins(Class<? extends Plugin>... klass);
    }

    private static class KernelBuilderImpl implements KernelBuilderWithPlugin
    {

        private final Kernel kernel;

        /**
         * 
         */
        public KernelBuilderImpl(String... keyValues)
        {
            kernel = new Kernel(keyValues);
        }

        @Override
        public Kernel build()
        {

            if (Kernel.kernel == null)
            {
                Kernel.kernel = kernel;
                return kernel;
            }
            else
            {
                throw new KernelException("Kernel cannot be created more than once");
            }

        }

        public KernelBuilder withPlugins(java.lang.Class<? extends Plugin>... klass)
        {
            kernel.addPlugins(klass);
            return (KernelBuilder) this;
        }

    }

    @SuppressWarnings("unchecked")
    private <T> T newInstance(Class<?> klass)
    {
        T instance = null;

        try
        {
            instance = (T) klass.newInstance();
        }
        catch (InstantiationException e)
        {
            logger.error("Error when instantiating class " + klass, e);
        }
        catch (IllegalAccessException e)
        {
            logger.error("Error when instantiating class " + klass, e);
        }

        return instance;
    }

    /**
     * TODO : can not add plugin if started
     * 
     * @param klass
     */
    void addPlugins(Class<? extends Plugin>... klass)
    {
        for (Class<? extends Plugin> class1 : klass)
        {

            Plugin plugin = newInstance(class1);
            if (plugin == null)
            {
                throw new KernelException("Plugin %s can not be instanciated", (Object) klass);
            }
            else
            {
                pluginsToAdd.put(plugin.name(), plugin);
            }
        }
    }

}
