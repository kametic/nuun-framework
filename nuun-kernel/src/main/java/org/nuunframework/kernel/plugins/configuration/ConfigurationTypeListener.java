package org.nuunframework.kernel.plugins.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.apache.commons.configuration.Configuration;
import org.nuunframework.kernel.commons.AssertUtils;

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

//                if (   field.isAnnotationPresent(NuunProperty.class)  )
                if (   annotationPresent(field , NuunProperty.class)  )
                {
                    encounter.register(new ConfigurationMembersInjector<T>(field, configuration));
                }
            }
        }
    }
    
    private boolean annotationPresent(Field field , Class<? extends Annotation> annoClass)
    {
        for (Annotation anno : field.getAnnotations() )
        {
            if ( AssertUtils.hasAnnotationDeep(anno.annotationType(), annoClass) )
            {
                System.err.println(">> " + anno.annotationType());
                return true;
            }
        }
        
        return false;
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
