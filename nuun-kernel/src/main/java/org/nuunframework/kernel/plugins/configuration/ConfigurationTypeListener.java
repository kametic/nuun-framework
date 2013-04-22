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
