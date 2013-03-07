/**
 * 
 */
package org.nuunframework.kernel;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;
import java.util.Set;

import org.apache.commons.collections.iterators.ArrayIterator;
import org.nuunframework.kernel.commons.graph.Graph;
import org.nuunframework.kernel.context.Context;
import org.nuunframework.kernel.context.InitContext;
import org.nuunframework.kernel.context.InitContextInternal;
import org.nuunframework.kernel.internal.InternalKernelGuiceModule;
import org.nuunframework.kernel.plugin.Plugin;
import org.nuunframework.kernel.plugin.provider.DependencyInjectionProvider;
import org.nuunframework.kernel.plugin.request.BindingRequest;
import org.nuunframework.kernel.plugin.request.ClasspathScanRequest;
import org.nuunframework.kernel.plugin.request.KernelParamsRequest;
import org.nuunframework.kernel.plugin.request.KernelParamsRequestType;
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

    public static final String                      NUUN_ROOT_PACKAGE      = "nuun.root.package";
    public static final String                      NUUN_NUM_CP_PATH       = "nuun.num.classpath.path";
    public static final String                      NUUN_CP_PATH_PREFIX    = "nuun.classpath.path.prefix-";
    private Logger                                  logger                 = LoggerFactory.getLogger(Kernel.class);
    private static Kernel                           kernel;

    private final String                            NUUN_PROPERTIES_PREFIX = "nuun-";

    private ServiceLoader<Plugin>                   pluginLoader;
    private boolean                                 spiPluginEnabled       = true;
    private Map<String, Plugin>                     plugins                = Collections.synchronizedMap(new HashMap<String, Plugin>()); //
    private Map<String, Plugin>                     pluginsToAdd           = Collections.synchronizedMap(new HashMap<String, Plugin>()); //

    private final InitContextInternal               initContext;
    private Injector                                mainInjector;
    private final Map<String, String>               kernelParams;

    private boolean                                 started                = false;
    private boolean                                 initialized            = false;
    private Context                                 context;
    private Collection<DependencyInjectionProvider> dependencyInjectionProviders;
    private Object                                  containerContext;
	private ArrayList<Plugin>                       orderedPlugins;

    private Kernel(String... keyValues)
    {

        kernelParams = new HashMap<String, String>();

        this.initContext = new InitContextInternal(NUUN_PROPERTIES_PREFIX, kernelParams);

        @SuppressWarnings("unchecked")
        Iterator<String> it = new ArrayIterator(keyValues);
        while (it.hasNext())
        {
            String key = it.next();
            String value = "";
            if (it.hasNext())
                value = it.next();
            logger.info("Adding {} = {} as param to kernel", key, value);
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
                    this.initContext.addPackageRoot(pack);
                }
            }
        }
    }

    public boolean isStarted()
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
            mainInjector = Guice.createInjector(new InternalKernelGuiceModule(initContext));
            context = mainInjector.getInstance(Context.class);
            
            // 1) inject plugins via injector
            // 2) inject context via injector
            // 2) start them

            for (Plugin plugin : orderedPlugins)
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
        // plugin from kernel call api
        Iterator<Plugin> iterator1 = pluginsToAdd.values().iterator();

        List<Iterator<Plugin>> iterators;

        // TODO add unit and test integration test for this
        if (spiPluginEnabled)
        {
            pluginLoader = ServiceLoader.load(Plugin.class, Thread.currentThread().getContextClassLoader());

            // plugin from service loader
            Iterator<Plugin> iterator2 = pluginLoader.iterator();
            iterators = Arrays.asList(iterator2, iterator1);
        }
        else
        {
            iterators = Arrays.asList(iterator1);
        }

        List<Class<? extends Plugin>> pluginClasses = new ArrayList<Class<? extends Plugin>>();
        // we initialize plugins
        for (Iterator<Plugin> iterator : iterators)
        {
            Plugin plugin;
            while (iterator.hasNext())
            {
                plugin = iterator.next();
                logger.info("checking Plugin {}.", plugin.name());
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
                            logger.error("plugin {} miss parameter/s : {}", plugin.name(), kernelParamsRequests.toString());
                            throw new KernelException("plugin " + plugin.name() + " miss parameter/s : " + kernelParamsRequests.toString());
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
            Collection<Class<? extends Plugin>> pluginDependenciesRequired = plugin.pluginDependenciesRequired();

            if (pluginDependenciesRequired != null && !pluginDependenciesRequired.isEmpty() && !pluginClasses.containsAll(pluginDependenciesRequired))
            {
                logger.error("plugin {} misses the following plugin/s as dependency/ies {}", plugin.name(), pluginDependenciesRequired.toString());
                throw new KernelException("plugin %s misses the following plugin/s as dependency/ies %s", plugin.name(), pluginDependenciesRequired.toString());

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
                this.initContext.addPropertiesPrefix(plugin.pluginPropertiesPrefix());
            }

            // Configure package root
            if (!Strings.isNullOrEmpty(plugin.pluginPackageRoot()))
            {
                this.initContext.addPackageRoot(plugin.pluginPackageRoot());
            }

            if (plugin.classpathScanRequests() != null && plugin.classpathScanRequests().size() > 0)
            {
                for (ClasspathScanRequest request : plugin.classpathScanRequests())
                {
                    switch (request.requestType)
                    {
                        case ANNOTATION_TYPE:
                            this.initContext.addAnnotationTypesToScan((Class<? extends Annotation>) request.objectRequested);
                            break;
                        case ANNOTATION_REGEX_MATCH:
                            this.initContext.addAnnotationRegexesToScan((String) request.objectRequested);
                            break;
                        case SUBTYPE_OF_BY_CLASS:
                            this.initContext.addParentTypeClassToScan((Class<?>) request.objectRequested);
                            break;
                        case SUBTYPE_OF_BY_REGEX_MATCH:
                            this.initContext.addParentTypeRegexesToScan((String) request.objectRequested);
                            break;
                        case RESOURCES_REGEX_MATCH:
                            this.initContext.addResourcesRegexToScan((String) request.objectRequested);
//                            this.initContext.addTypeClassToScan((Class<?>) request.objectRequested);
                            break;
                        case TYPE_OF_BY_REGEX_MATCH:
                            this.initContext.addTypeRegexesToScan((String) request.objectRequested);
                            break;
                        case VIA_SPECIFICATION: // pas encore pluggé
                            this.initContext.addSpecificationToScan( request.specification);
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
                            this.initContext.addAnnotationTypesToBind((Class<? extends Annotation>) request.requestedObject);
                            break;
                        case ANNOTATION_REGEX_MATCH:
                            this.initContext.addAnnotationRegexesToBind((String) request.requestedObject);
                            break;
                        case SUBTYPE_OF_BY_CLASS:
                            this.initContext.addParentTypeClassToBind((Class<?>) request.requestedObject);
                            break;
                        case SUBTYPE_OF_BY_REGEX_MATCH:
                            this.initContext.addTypeRegexesToBind((String) request.requestedObject);
                            break;
                        case VIA_SPECIFICATION:
                            this.initContext.addSpecificationToBind(request.specification , request.requestedScope);
                            break;
                        default:
                            logger.warn("{} is not a BindingRequestType o_O !", request.requestType);
                            break;
                    }
                }
            }
        }

//        initContext.setContainerContext(this.containerContext);
        Collection<DependencyInjectionProvider> dependencyInjectionProviders = new HashSet<DependencyInjectionProvider>();
        
        // INITIALISATION
        // We pass the container context object for plugin
        for (Plugin plugin : plugins.values())
        {
            logger.info("Get additional classpath to scan from Plugin {}.", plugin.name());
            Set<URL> computeAdditionalClasspathScan = plugin.computeAdditionalClasspathScan(containerContext);
            if (computeAdditionalClasspathScan != null && computeAdditionalClasspathScan.size() > 0)
            {
                logger.info("Adding from Plugin {} start.", plugin.name());
                for (URL url : computeAdditionalClasspathScan)
                {
                    logger.debug(url.toExternalForm());
                }
                logger.info("Adding from Plugin {} end. {} elements.", plugin.name() , "" + computeAdditionalClasspathScan.size());
                initContext.addClasspathsToScan(computeAdditionalClasspathScan);
            }
            // Convert dependency manager classes to instances //
            DependencyInjectionProvider iocProvider = plugin.dependencyInjectionProvider();
            if (iocProvider != null) 
            {
                dependencyInjectionProviders.add(iocProvider);
            }
        }

//        // We also want to scan DependencyInjectionProvider before the classpath scan
//        initContext.addParentTypeClassToScan(DependencyInjectionProvider.class);

        // We launch classpath scan and store results of requests
        this.initContext.executeRequests();

        // INITIALISATION
//        ArrayList<Plugin> orderedPlugins = sortPlugins (plugins.values());
        ArrayList<Plugin> unOrderedPlugins = new ArrayList<Plugin> (plugins.values());
        
        
        logger.debug("unordered plugins: " + unOrderedPlugins);
        orderedPlugins = sortPlugins(unOrderedPlugins); 
        logger.debug("ordered plugins: " + orderedPlugins);
        for (Plugin plugin : orderedPlugins)
        {
            InitContext actualInitContext = this.initContext;
            if ( plugin.pluginDependenciesRequired() != null && !plugin.pluginDependenciesRequired().isEmpty() )
            {
                Collection<Plugin> selectedPlugins = filterPlugins(plugins.values() , plugin.pluginDependenciesRequired() );
                actualInitContext = proxyfy(initContext, selectedPlugins);
            }
            
            logger.info("initializing Plugin {}.", plugin.name());
            plugin.init(actualInitContext);
        }

        // After been initialized plugin can give they module
        // Configure module //
        
        for (Plugin plugin : orderedPlugins)
        {
            Object pluginDependencyInjectionDef = plugin.dependencyInjectionDef();
            if (pluginDependencyInjectionDef != null)
            {
                if (pluginDependencyInjectionDef instanceof com.google.inject.Module)
                {
                    this.initContext.addChildModule(com.google.inject.Module.class.cast(pluginDependencyInjectionDef));
                }
                else
                {
                    DependencyInjectionProvider provider = null;
                    providerLoop: for (DependencyInjectionProvider providerIt : dependencyInjectionProviders)
                    {
                        if (providerIt.canHandle(pluginDependencyInjectionDef.getClass()))
                        {
                            provider = providerIt;
                            break providerLoop;
                        }
                    }
                    if (provider != null)
                    {
                        this.initContext.addChildModule(provider.convert(pluginDependencyInjectionDef));
                    }
                    else
                    {
                        logger.error("Kernel did not recognize module {} of plugin {}", pluginDependencyInjectionDef, plugin.name());
                        throw new KernelException("Kernel did not recognize module %s of plugin %s. Please provide a DependencyInjectionProvider.", pluginDependencyInjectionDef.toString(), plugin.name());
                    }
                }
            }
        }
    }
    
    private ArrayList<Plugin> sortPlugins(ArrayList<Plugin> unsortedPlugins)
    {
        Graph graph = new Graph(unsortedPlugins.size());  	
        ArrayList<Plugin> sorted = new ArrayList<Plugin>();
        Map<Integer, Plugin> idxPlug = new HashMap<Integer, Plugin>();
        Map<Character, Plugin> charPlug = new HashMap<Character, Plugin>();
        Map<Plugin, Integer> plugIdx = new HashMap<Plugin, Integer>();
        Map<Class<? extends Plugin >, Integer> classPlugIdx = new HashMap<Class<? extends Plugin >, Integer>();
        
    	// Add vertices
        for (int i = 0 ; i< unsortedPlugins.size() ; i ++)
        {
        	char c = ("" + i).charAt(0);
        	Plugin unsortedPlugin = unsortedPlugins.get(i);
        	Integer pluginIndex = graph.addVertex( c  );
        	
        	charPlug.put(c, unsortedPlugin);
        	idxPlug.put(pluginIndex, unsortedPlugin);
        	plugIdx.put(unsortedPlugin, pluginIndex);
        	classPlugIdx.put(unsortedPlugin.getClass(), pluginIndex);
        }
        
        // add edges
        for ( Entry<Integer, Plugin> entry : idxPlug.entrySet())
        {
        	Plugin source = entry.getValue();
        	
        	for (  Class<? extends Plugin > dependencyClass  : source.pluginDependenciesRequired())
        	{
        		int start = classPlugIdx.get(dependencyClass);
				int end = plugIdx.get(source);
				graph.addEdge(start, end );
        	}
        }
        
        // launch the algo
        char[] topologicalSort = graph.topologicalSort();
        
        for ( Character c : topologicalSort) 
        {
			sorted.add(charPlug.get(c));
		}
        
        return sorted;
    }


    private Collection<Plugin> filterPlugins(Collection<Plugin> collection, Collection<Class<? extends Plugin>> pluginDependenciesRequired)
    {
        Set<Plugin> filteredSet = new HashSet<Plugin>();
        
        for (Plugin plugin : collection)
        {
            if (pluginDependenciesRequired.contains(plugin.getClass()))
            {
                filteredSet.add(plugin);
            }
        }
        
        return filteredSet;
    }

    private InitContext proxyfy(final InitContext initContext, final Collection<Plugin> plugins)
    {
        return (InitContext) Proxy.newProxyInstance( //
                initContext.getClass().getClassLoader(), //
                new Class[] {
                    InitContext.class
                }, //
                new InvocationHandler()
                {

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
                    {
                        if (!method.getName().equals("pluginsRequired"))
                        {
                            return method.invoke(initContext, args);
                        }
                        else
                        {
                            return plugins;
                        }

                    }
                });
    }

    void createDependencyInjectionProvidersMap(Collection<Class<?>> dependencyInjectionProvidersClasses)
    {
        // Map<Class<?>, DependencyInjectionProvider> map = new HashMap<Class<?>,
        // DependencyInjectionProvider>();

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
    public synchronized static KernelBuilderWithPluginAndContext createKernel(String... keyValues)
    {
        return new KernelBuilderImpl(keyValues);
    }

    public static interface KernelBuilder
    {

        Kernel build();
    }

    public static interface KernelBuilderWithPluginAndContext extends KernelBuilderWithContainerContext, KernelBuilderWithPlugins
    {
    }

    public static interface KernelBuilderWithContainerContext extends KernelBuilder
    {

        KernelBuilderWithPlugins withContainerContext(Object containerContext);
    }

    public static interface KernelBuilderWithPlugins extends KernelBuilder
    {
        KernelBuilderWithPluginAndContext withPlugins(Class<? extends Plugin>... klass);

        KernelBuilderWithPluginAndContext withPlugins(Plugin... plugins);

        KernelBuilderWithPluginAndContext withoutSpiPluginsLoader();

    }

    private static class KernelBuilderImpl implements KernelBuilderWithPluginAndContext
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

        @Override
        public KernelBuilderWithPlugins withContainerContext(Object containerContext)
        {

            kernel.addContainerContext(containerContext);
            return (KernelBuilderWithPlugins) this;

        }

        public KernelBuilderWithPluginAndContext withPlugins(java.lang.Class<? extends Plugin>... klass)
        {
            kernel.addPlugins(klass);
            return  this;
        }

        public KernelBuilderWithPluginAndContext withPlugins(Plugin... plugin)
        {
            kernel.addPlugins(plugin);
            return this;
        }

        @Override
        public KernelBuilderWithPluginAndContext withoutSpiPluginsLoader()
        {
            kernel.spiPluginDisabled();
            return  this;
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

    void addContainerContext(Object containerContext)
    {
        this.containerContext = containerContext;

    }

    void spiPluginEnabled()
    {
        this.spiPluginEnabled = true;
    }

    void spiPluginDisabled()
    {
        this.spiPluginEnabled = false;
    }

    /**
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
                addPlugin(plugin);
            }
        }
    }

    void addPlugins(Plugin... plugins)
    {
        for (Plugin plugin : plugins)
        {
            addPlugin(plugin);
        }
    }

    void addPlugin(Plugin plugin)
    {
        if (!this.started)
        {
            pluginsToAdd.put(plugin.name(), plugin);
        }
        else
        {
            throw new KernelException("Plugin %s can not be added. Kernel already is started", plugin.name());
        }
    }

}
