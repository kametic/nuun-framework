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
package org.nuunframework.cli;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.nuunframework.kernel.commons.AssertUtils;
import org.nuunframework.kernel.plugin.PluginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * @author ejemba
 *
 */
public class NuunCliTypeListener implements TypeListener
{
    
    Logger logger = LoggerFactory.getLogger(NuunCliTypeListener.class);

//    private CommandLine commandLine;
    private Map<Class<?>, CommandLine> contextualCommandLineMap;

    /**
     * 
     */
    public NuunCliTypeListener(Map<Class<?>, CommandLine>  contextualCommandLineMap)
    {
        this.contextualCommandLineMap = contextualCommandLineMap;
        
    }

    /* (non-Javadoc)
     * @see com.google.inject.spi.TypeListener#hear(com.google.inject.TypeLiteral, com.google.inject.spi.TypeEncounter)
     */
    @Override
    public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter)
    {
        for (Class<?> c = type.getRawType(); c != Object.class; c = c.getSuperclass())
        {
            for (Field field : c.getDeclaredFields())
            {
                AnnotationPointer annotationPointer = new AnnotationPointer();
                if (annotationPresent(field, NuunOption.class, annotationPointer))
                {
                    if ( AssertUtils.isEquivalent(NuunOption.class, annotationPointer.annotation.annotationType()) )
                    {
                        if  (this.contextualCommandLineMap.get(c) != null )
                           encounter.register(new NuunOptionMembersInjector<I>(field, this.contextualCommandLineMap.get(c) ,  annotationPointer.annotation));
                        else
                            logger.info("warning : no commandline for context " + c);
                    }
                    else 
                    {
                        throw new PluginException("Annotation class %s is not compatible with %s. Please check it.", annotationPointer.annotation.annotationType().getCanonicalName() , NuunOption.class.getCanonicalName());
                    }
                }
                if (annotationPresent(field, NuunArgs.class, annotationPointer))
                {
                    if ( AssertUtils.isEquivalent(NuunArgs.class, annotationPointer.annotation.annotationType()) )
                    {
                        if  (this.contextualCommandLineMap.get(c) != null )
                           encounter.register(new NuunArgsMembersInjector<I>(field, this.contextualCommandLineMap.get(c) ,  annotationPointer.annotation));
                        else
                            logger.info("warning no commandline for context " + c);
                    }
                    else 
                    {
                        throw new PluginException("Annotation class %s is not compatible with %s. Please check it.", annotationPointer.annotation.annotationType().getCanonicalName() , NuunOption.class.getCanonicalName());
                    }
                }
            }
        }
    }
    
    
    static boolean annotationPresent(Field field , Class<? extends Annotation> annoClass, AnnotationPointer annotationPointer)
    {
        for (Annotation anno : field.getAnnotations() )
        {
            if ( AssertUtils.hasAnnotationDeep(anno.annotationType(), annoClass) )
            {
                annotationPointer.annotation = anno;
                return true;
            }
        }
        
        return false;
    }
    
    
    
    static class AnnotationPointer {
        public Annotation annotation;
    }
    
}
