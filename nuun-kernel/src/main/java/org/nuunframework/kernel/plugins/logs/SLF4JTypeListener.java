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
                return true;
            }
        }
        
        return false;
    }
    
}
