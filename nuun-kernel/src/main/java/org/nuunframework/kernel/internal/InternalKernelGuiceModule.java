package org.nuunframework.kernel.internal;

import static org.reflections.ReflectionUtils.withAnnotation;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.nuunframework.kernel.context.Context;
import org.nuunframework.kernel.context.ContextInternal;
import org.nuunframework.kernel.context.InitContextInternal;
import org.nuunframework.kernel.plugins.configuration.ConfigurationGuiceModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.util.Providers;

/**
 * 
 * Bootstrap Plugin needed to initialize an application. Propose
 * 
 * 
 * @author ejemba
 * 
 */
public class InternalKernelGuiceModule extends AbstractModule
{

    private Logger        logger                = LoggerFactory.getLogger(InternalKernelGuiceModule.class);

    private final InitContextInternal currentContext;
    
    public InternalKernelGuiceModule( InitContextInternal kernelContext )
    {
        this.currentContext = kernelContext ;
        
    }

    @Override
    protected final void configure()
    {
        // All bindings will be needed explicitely.
        // this simple line make the framework bullet prrof
        binder().requireExplicitBindings();
        // We ContextInternal as implemetation of Context
        bind(Context.class).to(ContextInternal.class);
        
        // Bind Types, Subtypes from classpath
        // ===================================
        bindFromClasspath();
        // TODO remove from here 
        configureProperties();
        // Start Plugins
    }

    @SuppressWarnings({
        "unchecked", "rawtypes"
    })
    private void bindFromClasspath()
    {
        List<Class<?>> classes = this.currentContext.classesToBind();
        
        // Install from  
        // ============
        for (Module moduleInstance : currentContext.moduleResults())
        {
            logger.info("installing module 1 {}",  moduleInstance ); 
            install(moduleInstance);
        }
        
        Provider nullProvider = Providers.of(null);
        
        for (Class<?> classpathClass : classes)
        {
//            if (Plugin.class.isAssignableFrom(classpathClass))
//            {
//                try
//                {
//                    logger.info("installing module 2 {}", classpathClass.getName());
//                    install((Plugin) classpathClass.newInstance());
//                }
//                catch (InstantiationException e)
//                {
//                    logger.error("Error on module installation ", classpathClass.getName(), e);
//                }
//                catch (IllegalAccessException e)
//                {
//                    logger.error("Error on module installation ", classpathClass.getName(), e);
//                }
//            }
//            else
//            {
                logger.info("binding {}", classpathClass.getName());
                if ( !  (classpathClass.isInterface() &&  withAnnotation( Nullable.class ).apply(classpathClass)) )
                {
                    bind(classpathClass);
                }
                else
                {
                    bind(classpathClass).toProvider(nullProvider);
                }
//            }
        }
    }

    private void configureProperties()
    {

        // find all properties classes in the classpath
        Collection<String> propertiesFiles = this.currentContext.propertiesFiles();        

        // add properties from plugins
        CompositeConfiguration configuration = new CompositeConfiguration();
        for (String propertiesFile : propertiesFiles)
        {
            logger.info("adding {} to module", propertiesFile);
            configuration.addConfiguration(configuration(propertiesFile));
        }
        install(new ConfigurationGuiceModule(configuration));
    }

    /**
     * reach properties file from classpath.
     * 
     * @param filePathInClasspath
     * @return return an Apache Configuration interface
     */
    private Configuration configuration(String filePathInClasspath)
    {
        try
        {
            return new PropertiesConfiguration(filePathInClasspath);
        }
        catch (ConfigurationException e)
        {
            e.printStackTrace();
            throw new IllegalStateException("Error in module initialization : Properties can not be initialized");
        }

    }

//    protected void bindSubTypesOf(Class<?> cläss)
//    {
//        this.currentContext.parentTypesClasses.add(cläss);
//    }
//
//    protected void bindAnnotationClass(Class<? extends Annotation> cläss)
//    {
//        this.currentContext.annotationTypes.add(cläss);
//    }
//
//    protected void bindAnnotationName(String className)
//    {
//        this.currentContext.annotationNames.add(className);
//    }

}
