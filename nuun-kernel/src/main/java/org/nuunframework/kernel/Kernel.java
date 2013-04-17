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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
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
import org.nuunframework.kernel.plugin.InitState;
import org.nuunframework.kernel.plugin.Plugin;
import org.nuunframework.kernel.plugin.RoundEnvironementInternal;
import org.nuunframework.kernel.plugin.provider.DependencyInjectionProvider;
import org.nuunframework.kernel.plugin.request.BindingRequest;
import org.nuunframework.kernel.plugin.request.ClasspathScanRequest;
import org.nuunframework.kernel.plugin.request.KernelParamsRequest;
import org.nuunframework.kernel.plugin.request.KernelParamsRequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
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
    private final int MAXIMAL_ROUND_NUMBER                                 = 50;
    private Logger                                  logger                 = LoggerFactory.getLogger(Kernel.class);
    private static Kernel                           kernel;

    private final String                            NUUN_PROPERTIES_PREFIX = "nuun-";

    private ServiceLoader<Plugin>                   pluginLoader;
    private boolean                                 spiPluginEnabled       = true;
    private Map<String, Plugin>                     plugins                = Collections.synchronizedMap(new HashMap<String, Plugin>()); //
    private Map<String, Plugin>                     pluginsToAdd           = Collections.synchronizedMap(new HashMap<String, Plugin>()); //

    private final InitContextInternal               initContext;
    private Injector                                mainInjector;
    private final AliasMap                          kernelParamsAndAlias;

    private boolean                                 started                = false;
    private boolean                                 initialized            = false;
    private Context                                 context;
    private Collection<DependencyInjectionProvider> dependencyInjectionProviders;
    private Object                                  containerContext;
    private ArrayList<Plugin>                       orderedPlugins;

    private Collection<DependencyInjectionProvider> globalDependencyInjectionProviders;
    private List<Iterator<Plugin>>                  pluginIterators;
    private List<Plugin>                            fetchedPlugins;
    private Set<URL>                                globalAdditionalClasspath;
    private RoundEnvironementInternal roundEnv;

    private Kernel(String... keyValues)
    {
        kernelParamsAndAlias = new AliasMap();

        this.initContext = new InitContextInternal(NUUN_PROPERTIES_PREFIX, kernelParamsAndAlias);

        @SuppressWarnings("unchecked")
        Iterator<String> it = new ArrayIterator(keyValues);
        while (it.hasNext())
        {
            String key = it.next();
            String value = "";
            if (it.hasNext())
                value = it.next();
            logger.info("Adding {} = {} as param to kernel", key, value);
            kernelParamsAndAlias.put(key, value);
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
            fetchPlugins();
            computeAliases();
            initRoundEnvironment();
            checkPlugins();
            fetchGlobalParametersFromPlugins();
            initPlugins();
            initialized = true;
        }
        else
        {
            throw new KernelException("Kernel is already initialized");
        }
    }

    private void initRoundEnvironment()
    {
        // we initialize plugins
        roundEnv = new RoundEnvironementInternal();

        for (Plugin plugin : fetchedPlugins)
        {
            // we pass the roundEnvironment
            plugin.provideRoundEnvironment(roundEnv);

        }
        
    }

    private void fetchGlobalParametersFromPlugins()
    {
        globalDependencyInjectionProviders = new HashSet<DependencyInjectionProvider>();
        globalAdditionalClasspath = Sets.newHashSet();
        
  
        
        // Constants from plugin outside rounds
        // We pass the container context object for plugin
        for (Plugin plugin : plugins.values())
        {
            plugin.provideContainerContext(containerContext);
            
            String name = plugin.name();
            logger.info("Get additional classpath to scan from Plugin {}.", name);

            Set<URL> computeAdditionalClasspathScan = plugin.computeAdditionalClasspathScan();
            if (computeAdditionalClasspathScan != null && computeAdditionalClasspathScan.size() > 0)
            {
                logger.info("Adding from Plugin {} start.", name);
                for (URL url : computeAdditionalClasspathScan)
                {
                    globalAdditionalClasspath.add(url);
                    logger.debug(url.toExternalForm());
                }
                logger.info("Adding from Plugin {} end. {} elements.", name, "" + computeAdditionalClasspathScan.size());
            }
            // Convert dependency manager classes to instances //
            DependencyInjectionProvider iocProvider = plugin.dependencyInjectionProvider();
            if (iocProvider != null)
            {
                globalDependencyInjectionProviders.add(iocProvider);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void fetchPlugins()
    {
        // plugin from kernel call api
        Iterator<Plugin> iterator1 = pluginsToAdd.values().iterator();

        // TODO add unit and test integration test for this
        if (spiPluginEnabled)
        {
            pluginLoader = ServiceLoader.load(Plugin.class, Thread.currentThread().getContextClassLoader());

            // plugin from service loader
            Iterator<Plugin> iterator2 = pluginLoader.iterator();
            pluginIterators = Arrays.asList(iterator2, iterator1);
        }
        else
        {
            pluginIterators = Arrays.asList(iterator1);
        }

        fetchedPlugins = new LinkedList<Plugin>();

        for (Iterator<Plugin> iterator : pluginIterators)
        {
            Plugin plugin;
            while (iterator.hasNext())
            {
                plugin = iterator.next();
                fetchedPlugins.add(plugin);
            }

        }
    }

    private void computeAliases()
    {
        // Compute alias
        for (Plugin plugin : fetchedPlugins)
        {
            Map<String, String> pluginKernelParametersAliases = plugin.kernelParametersAliases();
            for (Entry<String, String> entry : pluginKernelParametersAliases.entrySet())
            {
                // entry.getValue() is the alias to give
                // entry.getKey() is the key to alias
                
                logger.info("Adding alias parameter \"{}\" to key \"{}\"." , entry.getKey() ,  entry.getValue());
                kernelParamsAndAlias.putAlias(entry.getKey() , entry.getValue());
            }
        }

        //
        if (kernelParamsAndAlias.containsKey(NUUN_ROOT_PACKAGE))
        {
            String tmp = kernelParamsAndAlias.get(NUUN_ROOT_PACKAGE);

            String[] packages = null;
            if (tmp != null)
            {
                packages = tmp.split(",");

                for (String pack : packages)
                {
                    logger.info("Adding {} as nuun.package.root", pack);
                    this.initContext.addPackageRoot(pack.trim());
                }
            }
        }
    }

    // String getKernelParam(String key)
    // {
    //
    // }

    /**
     * 
     */
    private void checkPlugins()
    {
        logger.info("Plugins initialisation ");
        plugins.clear();

        List<Class<? extends Plugin>> pluginClasses = new ArrayList<Class<? extends Plugin>>();

        for (Plugin plugin : fetchedPlugins) {                       
            
            String pluginName = plugin.name();
            logger.info("checking Plugin {}.", pluginName);
            if (!Strings.isNullOrEmpty(pluginName))
            {
                Object ok = plugins.put(pluginName, plugin);
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

                    if (kernelParamsAndAlias.containsAllKeys(computedMandatoryParams))
                    // if (kernelParams.keySet().containsAll(computedMandatoryParams))
                    {
                        pluginClasses.add(plugin.getClass());
                    }
                    else
                    {
                        logger.error("plugin {} miss parameter/s : {}", pluginName, kernelParamsRequests.toString());
                        throw new KernelException("plugin " + pluginName + " miss parameter/s : " + kernelParamsRequests.toString());
                    }

                }
                else
                {
                    logger.error("Can not have 2 Plugin {} of the same type {}. please fix this before the kernel can start.", pluginName, plugin.getClass()
                            .getName());
                    throw new KernelException(
                            "Can not have 2 Plugin %s of the same type %s. please fix this before the kernel can start.", pluginName, plugin.getClass()
                                    .getName());
                }
            }
            else
            {
                logger.warn("Plugin {} has no correct name it won't be installed.", plugin.getClass());
                throw new KernelException("Plugin %s has no correct name it won't be installed.", pluginName);
            }
            
        }

        // Check for required and dependent plugins 
        for (Plugin plugin : plugins.values())
        {
            {
                Collection<Class<? extends Plugin>> pluginDependenciesRequired = plugin.requiredPlugins();
    
                if (pluginDependenciesRequired != null && !pluginDependenciesRequired.isEmpty() && !pluginClasses.containsAll(pluginDependenciesRequired))
                {
                    logger.error("plugin {} misses the following plugin/s as dependency/ies {}", plugin.name(), pluginDependenciesRequired.toString());
                    throw new KernelException("plugin %s misses the following plugin/s as dependency/ies %s", plugin.name(), pluginDependenciesRequired.toString());
                }
            }
            
            {
                Collection<Class<? extends Plugin>> dependentPlugin = plugin.dependentPlugins();

                if (dependentPlugin != null && !dependentPlugin.isEmpty() && !pluginClasses.containsAll(dependentPlugin))
                {
                    logger.error("plugin {} misses the following plugin/s as dependee/s {}", plugin.name(), dependentPlugin.toString());
                    throw new KernelException("plugin %s misses the following plugin/s as dependee/s %s", plugin.name(), dependentPlugin.toString());
                }
            }
        }
    }

    public synchronized void start()
    {
        if (initialized)
        {
            // All bindings will be computed
            mainInjector = Guice.createInjector(new InternalKernelGuiceModule(initContext));

            // Here we can pass the mainInjector to the non guice modules

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
    private void initPlugins()
    {

        
        // We reset the resettable element of initcontext
        this.initContext.reset();

        // Get plugins requests
        // ====================
        Collection<Plugin> globalPlugins = plugins.values(); // first round all plugins
        
        // We sort them
        ArrayList<Plugin> unOrderedPlugins = new ArrayList<Plugin>(globalPlugins);
        logger.info("unordered plugins: (" + unOrderedPlugins.size() + ") " + unOrderedPlugins);
        orderedPlugins = sortPlugins(unOrderedPlugins);
        logger.info("ordered plugins: ("  + orderedPlugins.size()  + ") "  +  orderedPlugins);
        Map<String,InitState> states = new HashMap<String, InitState>();
        
        ArrayList<Plugin> roundOrderedPlugins = new ArrayList<Plugin>(orderedPlugins);
        
        
        do 
        { // ROUND ITERATIONS
            
            // we update the number of initialization round.
            this.initContext.roundNumber(roundEnv.roundNumber());
            
            logger.info("ROUND " + roundEnv.roundNumber() + " of the kernel initialisation.");
            
            for (Plugin plugin : roundOrderedPlugins)
            {
                
                
                // Configure properties prefixes
                String pluginPropertiesPrefix = plugin.pluginPropertiesPrefix();
                if (!Strings.isNullOrEmpty(pluginPropertiesPrefix))
                {
                    this.initContext.addPropertiesPrefix(pluginPropertiesPrefix);
                }
                
                // Configure package root
                String pluginPackageRoot = plugin.pluginPackageRoot();
                if (!Strings.isNullOrEmpty(pluginPackageRoot))
                {
                    this.initContext.addPackageRoot(pluginPackageRoot);
                }
                
                Collection<ClasspathScanRequest> classpathScanRequests = plugin.classpathScanRequests();
                if (classpathScanRequests != null && classpathScanRequests.size() > 0)
                {
                    for (ClasspathScanRequest request : classpathScanRequests)
                    {
                        switch (request.requestType)
                        {
                            case ANNOTATION_TYPE:
                                this.initContext.addAnnotationTypesToScan((Class<? extends Annotation>) request.objectRequested);
                                break;
                            case ANNOTATION_REGEX_MATCH:
                                this.initContext.addAnnotationRegexesToScan((String) request.objectRequested);
                                break;
                                // case META_ANNOTATION_TYPE:
                                // this.initContext.addAnnotationTypesToScan((Class<? extends Annotation>)
                                // request.objectRequested);
                                // break;
                                // case META_ANNOTATION_REGEX_MATCH:
                                // this.initContext.addAnnotationRegexesToScan((String) request.objectRequested);
                                // break;
                            case SUBTYPE_OF_BY_CLASS:
                                this.initContext.addParentTypeClassToScan((Class<?>) request.objectRequested);
                                break;
                            case SUBTYPE_OF_BY_TYPE_DEEP:
                                this.initContext.addAncestorTypeClassToScan((Class<?>) request.objectRequested);
                                break;
                            case SUBTYPE_OF_BY_REGEX_MATCH:
                                this.initContext.addParentTypeRegexesToScan((String) request.objectRequested);
                                break;
                            case RESOURCES_REGEX_MATCH:
                                this.initContext.addResourcesRegexToScan((String) request.objectRequested);
                                // this.initContext.addTypeClassToScan((Class<?>) request.objectRequested);
                                break;
                            case TYPE_OF_BY_REGEX_MATCH:
                                this.initContext.addTypeRegexesToScan((String) request.objectRequested);
                                break;
                            case VIA_SPECIFICATION: // pas encore pluggé
                                this.initContext.addSpecificationToScan(request.specification);
                                break;
                            default:
                                logger.warn("{} is not a ClasspathScanRequestType a o_O", request.requestType);
                                break;
                        }
                    }
                }
                
                Collection<BindingRequest> bindingRequests = plugin.bindingRequests();
                if (bindingRequests != null && bindingRequests.size() > 0)
                {
                    for (BindingRequest request : bindingRequests)
                    {
                        switch (request.requestType)
                        {
                            case ANNOTATION_TYPE:
                                this.initContext.addAnnotationTypesToBind((Class<? extends Annotation>) request.requestedObject, request.requestedScope);
                                break;
                            case ANNOTATION_REGEX_MATCH:
                                this.initContext.addAnnotationRegexesToBind((String) request.requestedObject, request.requestedScope);
                                break;
                            case META_ANNOTATION_TYPE:
                                this.initContext.addMetaAnnotationTypesToBind((Class<? extends Annotation>) request.requestedObject, request.requestedScope);
                                break;
                            case META_ANNOTATION_REGEX_MATCH:
                                this.initContext.addMetaAnnotationRegexesToBind((String) request.requestedObject, request.requestedScope);
                                break;
                            case SUBTYPE_OF_BY_CLASS:
                                this.initContext.addParentTypeClassToBind((Class<?>) request.requestedObject, request.requestedScope);
                                break;
                            case SUBTYPE_OF_BY_TYPE_DEEP:
                                this.initContext.addAncestorTypeClassToBind((Class<?>) request.requestedObject, request.requestedScope);
                                break;
                            case SUBTYPE_OF_BY_REGEX_MATCH:
                                this.initContext.addTypeRegexesToBind((String) request.requestedObject, request.requestedScope);
                                break;
                            case VIA_SPECIFICATION:
                                this.initContext.addSpecificationToBind(request.specification, request.requestedScope);
                                break;
                            default:
                                logger.warn("{} is not a BindingRequestType o_O !", request.requestType);
                                break;
                        }
                    }
                }
            } // end plugin request
            
            for (URL url : globalAdditionalClasspath)
            {
                initContext.addClasspathToScan(url);
            }
            
            // We launch classpath scan and store results of requests
            this.initContext.executeRequests();
            
            // INITIALISATION
            
            for (Plugin plugin : roundOrderedPlugins)
            {
                InitContext actualInitContext = this.initContext;
                
                // TODO : we compute dependencies only in round 0 for first version , no other plugin will be given
                Collection<Class<? extends Plugin>> requiredPluginsClasses = plugin.requiredPlugins();
                Collection<Class<? extends Plugin>> dependentPluginsClasses = plugin.dependentPlugins();
                if (roundEnv.roundNumber() == 0 && (requiredPluginsClasses != null && !requiredPluginsClasses.isEmpty()) || (dependentPluginsClasses != null && !dependentPluginsClasses.isEmpty())  )
                {
                    Collection<Plugin> requiredPlugins = filterPlugins(globalPlugins, requiredPluginsClasses); 
                    Collection<Plugin> dependentPlugins = filterPlugins(globalPlugins, dependentPluginsClasses);
                    actualInitContext = proxyfy(initContext, requiredPlugins,dependentPlugins);
                }
                
                String name = plugin.name();
                logger.info("initializing Plugin {}.", name);
                InitState state = plugin.init(actualInitContext);
                states.put(name, state);
            }
            
            // After been initialized plugin can give they module
            // Configure module //
            ArrayList<Plugin> nextRoundOrderedPlugins = new ArrayList<Plugin>();
            
            for (Plugin plugin : roundOrderedPlugins)
            {
                String name = plugin.name();
                InitState state = states.get(name);
                
                if ( state == InitState.INITIALIZED )
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
                            providerLoop: for (DependencyInjectionProvider providerIt : globalDependencyInjectionProviders)
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
                                logger.error("Kernel did not recognize module {} of plugin {}", pluginDependencyInjectionDef, name);
                                throw new KernelException(
                                        "Kernel did not recognize module %s of plugin %s. Please provide a DependencyInjectionProvider.",
                                        pluginDependencyInjectionDef.toString(), name);
                            }
                        }
                    } //
                    
                }
                else
                { // the plugin is not initialized we add it for a new round
                    logger.info("Plugin " + name + " is not initialized. We set it for a new round");
                    nextRoundOrderedPlugins.add(plugin);
                }
            }
            roundOrderedPlugins = nextRoundOrderedPlugins;
            // increment round number
            roundEnv.incrementRoundNumber();
        } while( ! roundOrderedPlugins .isEmpty()  &&  roundEnv.roundNumber() < MAXIMAL_ROUND_NUMBER );
    }

    private ArrayList<Plugin> sortPlugins(ArrayList<Plugin> unsortedPlugins)
    {
        Graph graph = new Graph(unsortedPlugins.size());
        ArrayList<Plugin> sorted = new ArrayList<Plugin>();
        Map<Integer, Plugin> idxPlug = new HashMap<Integer, Plugin>();
        Map<Character, Plugin> charPlug = new HashMap<Character, Plugin>();
        Map<Plugin, Integer> plugIdx = new HashMap<Plugin, Integer>();
        Map<Class<? extends Plugin>, Integer> classPlugIdx = new HashMap<Class<? extends Plugin>, Integer>();

        // Add vertices
        for (short i = 0; i < unsortedPlugins.size(); i++)
        {
        	
        	char c = (char) i;
            Plugin unsortedPlugin = unsortedPlugins.get(i);
            Integer pluginIndex = graph.addVertex(c);

            charPlug.put(c, unsortedPlugin);
            idxPlug.put(pluginIndex, unsortedPlugin);
            plugIdx.put(unsortedPlugin, pluginIndex);
            classPlugIdx.put(unsortedPlugin.getClass(), pluginIndex);
        }

        // add edges
        for (Entry<Integer, Plugin> entry : idxPlug.entrySet())
        {
            Plugin source = entry.getValue();
            // based on required plugins
            for (Class<? extends Plugin> dependencyClass : source.requiredPlugins())
            {
                int start = classPlugIdx.get(dependencyClass);
                int end = plugIdx.get(source);
                graph.addEdge(start, end);
            }
            // based on dependent plugins
            for (Class<? extends Plugin> dependencyClass : source.dependentPlugins())
            {
                int start = plugIdx.get(source); // we inversed
                int end = classPlugIdx.get(dependencyClass);
                graph.addEdge(start, end);
            }
        }

        // launch the algo
        char[] topologicalSort = graph.topologicalSort();

        if (topologicalSort != null)
        {
            for (Character c : topologicalSort)
            {
                sorted.add(charPlug.get(c));
            }
        }
        else
        {
            throw new KernelException("Error when sorting plugins : either a Cycle in dependencies or another cause.");
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

    private InitContext proxyfy(final InitContext initContext, final Collection<Plugin> requiredPlugins , final  Collection<Plugin>  dependentPlugins)
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
                        if (method.getName().equals("pluginsRequired"))
                        {
                            return requiredPlugins;
                        }
                        else if (method.getName().equals("dependentPlugins"))
                            {
                                return dependentPlugins;
                            }
                            else
                        {
                            return method.invoke(initContext, args);
                        }

                    }
                });
    }

    void createDependencyInjectionProvidersMap(Collection<Class<?>> dependencyInjectionProvidersClasses)
    {

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
            return this;
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
            return this;
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

    static class AliasMap extends HashMap<String, String>
    {
        Map<String, String> aliases = new HashMap<String, String>();

        /**
         * 
         * 
         * @param key      the key to alias.
         * @param alias   the alias to give to the key.
         * @return
         */
        public String putAlias(String key, String alias)
        {
            if (super.containsKey(alias))
                throw new IllegalArgumentException("alias "+alias+" already exists in map.");
            return aliases.put(alias, key);
        }


        @Override
        public String get(Object key)
        {
            String keyAlias = aliases.get(key);
            if (keyAlias == null)
            {
                return super.get(key);
            }
            else
            {
                return super.get(keyAlias);
            }
        }
        
        public boolean containsAllKeys(Collection<String> computedMandatoryParams)
        {
            HashSet<String> allKeys = new HashSet<String>();
            allKeys.addAll( this.keySet() );
            allKeys.addAll(aliases.values());
            
            Collection<String> trans = new HashSet<String>();
            for (String s : computedMandatoryParams)
            {
                String string = aliases.get(s);
                if (string != null)
                {
                    trans.add(string);
                }
            }
            
            return allKeys.containsAll(trans);
        }
        
        @Override
        public boolean containsKey(Object key)
        {
            return  aliases.containsKey(key) ? true :  super.containsKey(key);
        }
        
    }

}
