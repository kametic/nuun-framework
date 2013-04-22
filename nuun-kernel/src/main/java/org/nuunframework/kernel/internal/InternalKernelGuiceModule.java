package org.nuunframework.kernel.internal;

import static org.reflections.ReflectionUtils.withAnnotation;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

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
    
    private boolean overriding = false;

    public InternalKernelGuiceModule(InitContextInternal kernelContext)
    {
        this.currentContext = kernelContext;

    }

    public InternalKernelGuiceModule overriding()
    {
        overriding = true;
        return this;
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
        List<Installable> installableList = new ArrayList<Installable>();
        Map<Class<?>, Object> classesWithScopes = this.currentContext.classesWithScopes();
        
        if (! overriding )
        {
            Collection<Class<?>> classes = this.currentContext.classesToBind();
            
            for (Object o : classes)
            {
                installableList.add(new Installable(o));
            }
            for (Object o : currentContext.moduleResults())
            {
                installableList.add(new Installable(o));
            }
        }
        else
        {
            logger.info("Installing overriding modules");
            for (Object o : currentContext.moduleOverridingResults())
            {
                installableList.add(new Installable(o));
            }
        }
        
        
        Collections.sort(installableList , Collections.reverseOrder());
        
        Provider nullProvider = Providers.of(null);
        
		// We install modules and bind class in the right orders
        for (Installable installable : installableList)
        {
        	if (Module.class.isAssignableFrom(installable.inner.getClass()))
        	{ // install module
        		logger.info("installing module {}", (installable.inner));
        		install(Module.class.cast(installable.inner));
        		
        	}
        	if (installable.inner instanceof Class)
        	{ // bind object
        		
        		Class<?> classpathClass = Class.class.cast(installable.inner);
        		
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
        		
        	}
        }
    }
    
    class Installable implements Comparable<Installable>
    {
        
        Object inner;

        Installable (Object inner)
        {
            this.inner = inner;
            
        }
        
        @Override
        public int compareTo(Installable anInstallable)
        {
            Class<?> toCompare;
            Class<?> innerClass;
            
            
            // to compare inner is a class to bind
            if (anInstallable.inner instanceof Class)
            {
                toCompare = (Class<?>) anInstallable.inner;
            }
            else if (Module.class.isAssignableFrom(anInstallable.inner.getClass()))
            // inner is a module annotated
            {
                toCompare = anInstallable.inner.getClass();
            }
            else 
            {
            	throw new IllegalStateException("Object to compare is not a class nor a Module " + anInstallable);
            }

            // inner is a class to bind
            if (this.inner instanceof Class)
            {
            	innerClass = (Class<?>) this.inner;
            }
            else if (Module.class.isAssignableFrom(this.inner.getClass()))
            	// inner is a module annotated
            {
            	innerClass = this.inner.getClass();
            }
            else 
            {
            	throw new IllegalStateException("Object to compare is not a class nor a Module " + this);
            }
            
            return  computeOrder(innerClass).compareTo( computeOrder(toCompare) )  ;
        }
        
        @Override
        public String toString()
        {
            return inner.toString();
        }
    }
    
    Long computeOrder (Class<?> moduleCläss)    {
        
    	Long finalOrder = 0l;    	
    	boolean reachAtLeastOnce = false;
    	
        outer: for(Annotation annotation : moduleCläss.getAnnotations())
        {
            if ( Matchers.annotatedWith(Concern.class).matches(annotation.annotationType()) )
            {
                reachAtLeastOnce = true;
            	Concern concern = annotation.annotationType().getAnnotation(Concern.class);
                switch (concern.priority()) 
                {
                    case HIGHEST:
                        finalOrder += ( 2000 + concern.order() );
                        break;
                    case HIGHER:
                        finalOrder += ( 200 + concern.order() );
                        break;
                    case HIGH:
                        finalOrder += ( 20 + concern.order() );
                        break;
                    case NORMAL:
                        finalOrder += ( 0 + concern.order() );
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
                    default:
                        break;
                }
                
                break outer;
            }
        }
        
    	if (! reachAtLeastOnce) finalOrder = (long) 0;
    	
        return finalOrder;
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
