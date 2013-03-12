package org.nuunframework.kernel.internal;

import static org.reflections.ReflectionUtils.withAnnotation;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.Nullable;

import org.nuunframework.kernel.commons.AssertUtils;
import org.nuunframework.kernel.context.Context;
import org.nuunframework.kernel.context.ContextInternal;
import org.nuunframework.kernel.context.InitContextInternal;
import org.nuunframework.kernel.stereotype.Concern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.matcher.Matchers;
import com.google.inject.util.Providers;

/**
 * Bootstrap Plugin needed to initialize an application. Propose
 * 
 * @author ejemba
 */
public class InternalKernelGuiceModule extends AbstractModule
{

    private Logger                    logger = LoggerFactory.getLogger(InternalKernelGuiceModule.class);

    private final InitContextInternal currentContext;

    public InternalKernelGuiceModule(InitContextInternal kernelContext)
    {
        this.currentContext = kernelContext;

    }

    @Override
    protected final void configure()
    {
        // All bindings will be needed explicitely.
        // this simple line makes the framework bullet-proof !
        binder().requireExplicitBindings();
        // We ContextInternal as implemetation of Context
        bind(Context.class).to(ContextInternal.class);

        // Bind Types, Subtypes from classpath
        // ===================================
        bindFromClasspath();

        // Start Plugins
    }

    @SuppressWarnings({
            "unchecked", "rawtypes"
    })
    private void bindFromClasspath()
    {
        List<Class<?>> classes = this.currentContext.classesToBind();
        Map<Class<?>, Object> classesWithScopes = this.currentContext.classesWithScopes();
        
        SortedSet<Object> sortedSet = new TreeSet<Object>();
        for (Object o : classes)
        {
            sortedSet.add(new Installable(o));
        }
        for (Object o : currentContext.moduleResults())
        {
            sortedSet.add(new Installable(o));
        }
        for (Object object : sortedSet)
        {
            logger.error( "  >>> " + object );
        }
        
        // Install from
        // ============
        for (Module moduleInstance : currentContext.moduleResults())
        {
            logger.info("installing module 1 {}", moduleInstance);
            install(moduleInstance);
        }

        Provider nullProvider = Providers.of(null);

        for (Class<?> classpathClass : classes)
        {
            Object scope = classesWithScopes.get(classpathClass);
            
            if (!(classpathClass.isInterface() && withAnnotation(Nullable.class).apply(classpathClass)))
            {
                if (scope == null)
                {
                    logger.info("binding {} with no scope.", classpathClass.getName());
                    bind(classpathClass);
                }
                else
                {
                    logger.info("binding {} in scope {}.", classpathClass.getName() , scope.toString());
                    bind(classpathClass).in((Scope) scope); 
                    
                }
            }
            else
            {
                bind(classpathClass).toProvider(nullProvider);
            }
            // }
        }
    }
    
    class Installable implements Comparable<Object>
    {
        
        Object inner;

        Installable (Object inner)
        {
            this.inner = inner;
            
        }
        
        @Override
        public int compareTo(Object o)
        {
            Class<?> toCompare;
            if (o instanceof Class)
            {
                toCompare = (Class<?>) o;
            }
            else 
            {
                toCompare = o.getClass();
            }
            
            return   computeOrder(inner.getClass())   -     computeOrder(toCompare)  ;
        }
        
        @Override
        public String toString()
        {
            return inner.toString();
        }
    }
    
    private Integer computeOrder (Class<?> moduleCläss)    {
        
        for(Annotation annotation : moduleCläss.getAnnotations())
        {
            if ( Matchers.annotatedWith(Concern.class).matches(annotation.annotationType()) )
            {
                Concern concern = annotation.annotationType().getAnnotation(Concern.class);
                Integer finalOrder = 0;
                switch (concern.priority()) 
                {
                    case HIGH:
                        finalOrder += ( 20 + concern.order() );
                        break;
                    case HIGHER:
                        finalOrder += ( 200 + concern.order() );
                        break;
                    case HIGHEST:
                        finalOrder += ( 2000 + concern.order() );
                        break;
                    case LOW:
                        finalOrder -= ( 20 + concern.order() );
                        break;
                    case LOWER:
                        finalOrder -= ( 200 + concern.order() );
                        break;
                    case LOWEST:
                        finalOrder -= ( 2000 + concern.order() );
                        break;
                    case NORMAL:
                        finalOrder += ( 0 + concern.order() );
                        break;
                    default:
                        break;
                    
                }
                return finalOrder;
            }
        }
        
        return Integer.MIN_VALUE;
    }

    public static boolean hasAnnotationDeep(Class<?> memberDeclaringClass, Class<? extends Annotation> klass)
    {

        if (memberDeclaringClass.equals(klass))
        {
            return true;
        }

        for (Annotation anno : memberDeclaringClass.getAnnotations())
        {
            Class<? extends Annotation> annoClass = anno.annotationType();
            if (!annoClass.getPackage().getName().startsWith("java.lang") && hasAnnotationDeep(annoClass, klass))
            {
                return true;
            }
        }

        return false;
    }
    
    
    // private void configureProperties()
    // {
    //
    // // find all properties classes in the classpath
    // Collection<String> propertiesFiles = this.currentContext.propertiesFiles();
    //
    // // add properties from plugins
    // CompositeConfiguration configuration = new CompositeConfiguration();
    // for (String propertiesFile : propertiesFiles)
    // {
    // logger.info("adding {} to module", propertiesFile);
    // configuration.addConfiguration(configuration(propertiesFile));
    // }
    // install(new ConfigurationGuiceModule(configuration));
    // }

    // protected void bindSubTypesOf(Class<?> cläss)
    // {
    // this.currentContext.parentTypesClasses.add(cläss);
    // }
    //
    // protected void bindAnnotationClass(Class<? extends Annotation> cläss)
    // {
    // this.currentContext.annotationTypes.add(cläss);
    // }
    //
    // protected void bindAnnotationName(String className)
    // {
    // this.currentContext.annotationNames.add(className);
    // }

}
