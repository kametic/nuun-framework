package org.nuunframework.kernel.plugins.logs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.nuunframework.kernel.commons.AssertUtils;
import org.slf4j.Logger;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public class SLF4JTypeListener implements TypeListener
{

    public <T> void hear(TypeLiteral<T> typeLiteral, TypeEncounter<T> typeEncounter)
    {

        for (Class<?> c = typeLiteral.getRawType(); c != Object.class; c = c.getSuperclass())
        {
            for (Field field : typeLiteral.getRawType().getDeclaredFields())
            {
                if (field.getType() == Logger.class &&  annotationPresent(field , NuunLog.class))
                {
                    typeEncounter.register(new SLF4JMembersInjector<T>(field));
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
    
}
