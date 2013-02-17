package org.nuunframework.kernel.plugins.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.apache.commons.configuration.Configuration;
import org.nuunframework.kernel.commons.AssertUtils;
import org.nuunframework.kernel.plugin.PluginException;

import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * Type listener for objects prepared for injection. Assigns injection policy
 * for each field annotated with {@link Conf}.
 * 
 * @author anatolich
 */
public class ConfigurationTypeListener implements TypeListener
{

    private final Configuration configuration;

    @Inject
    public ConfigurationTypeListener(Configuration configuration)
    {
        this.configuration = configuration;
    }

    public <T> void hear(TypeLiteral<T> type, TypeEncounter<T> encounter)
    {
        for (Class<?> c = type.getRawType(); c != Object.class; c = c.getSuperclass())
        {
            for (Field field : c.getDeclaredFields())
            {

                Holder h = new Holder();
                if (   annotationPresent(field , NuunProperty.class , h)  )
                {
                    if ( AssertUtils.isEquivalent(NuunProperty.class, h.annotation.annotationType()) )
                    {
                        encounter.register(new ConfigurationMembersInjector<T>(field, configuration , h.annotation));
                    }
                    else
                    {
                        throw new PluginException("Annotation class %s is not compatible with %s. Please check it.", h.annotation.annotationType().getCanonicalName() , NuunProperty.class.getCanonicalName());
                    }
                }
            }
        }
    }
    
    private boolean annotationPresent(Field field , Class<? extends Annotation> annoClass, Holder h)
    {

        for (Annotation anno : field.getAnnotations() )
        {
            if ( AssertUtils.hasAnnotationDeep(anno.annotationType(), annoClass) )
            {
                h.annotation = anno;
                return true;
            }
        }
        
        return false;
    }
    
    static class Holder {
        public Annotation annotation;
    }

    // public <T> void hear_(TypeLiteral<T> typeLiteral, TypeEncounter<T>
    // typeEncounter)
    // {
    // for (Class<?> c = typeLiteral.getRawType(); c != Object.class; c =
    // c.getSuperclass())
    // {
    // for (Field field : c.getDeclaredFields())
    // {
    // if (field.getType() == Logger.class
    // && field.isAnnotationPresent(InjectLogger.class))
    // {
    // typeEncounter.register(new Log4JMembersInjector<T>(field));
    // }
    // }
    // }
    // }

}
