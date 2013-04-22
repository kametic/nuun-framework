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
package org.nuunframework.kernel.context;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Collections2.transform;

import java.lang.annotation.Annotation;
import java.util.Collection;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.inject.Injector;
import com.google.inject.Key;

/**
 * @author Epo Jemba
 * 
 */
@Singleton
public class ContextInternal implements Context
{

    public final Injector mainInjector;

    /**
     * 
     */
    @Inject
    public ContextInternal(Injector mainInjector)
    {
        this.mainInjector = mainInjector;
    }

    // private void indexBindings()
    // {
    // for (Key key : mainInjector.getAllBindings().keySet())
    // {
    // key.getAnnotationType()
    // }
    // }

 
    @Override
    public Collection<Class<?>> getClassAnnotatedWith(Class<? extends Annotation> clazz)
    {
        Collection<Key<?>> resultKeys = filter(mainInjector.getAllBindings().keySet(), withAnnotation(clazz));
        return transform(resultKeys, fromKeyTClass());
    }

 
    @Override
    public Collection<Class<?>> getClassAnnotatedWithRegex(String regex)
    {
        Collection<Key<?>> resultKeys = filter(mainInjector.getAllBindings().keySet(), withAnnotationName(regex));
        return transform(resultKeys, fromKeyTClass());
    }

  
    @Override
    public Collection<Class<?>> getClassWithParentType(Class<?> parentClass)
    {
        Collection<Key<?>> resultKeys = filter(mainInjector.getAllBindings().keySet(), withParentType(parentClass));
        return transform(resultKeys, fromKeyTClass());
    }


    @Override
    public Collection<Class<?>> getClassTypeByRegex(String typeNameRegex)
    {
        Collection<Key<?>> resultKeys = filter(mainInjector.getAllBindings().keySet(), withTypeName(typeNameRegex));
        return transform(resultKeys, fromKeyTClass());
    }
    
    
    /** where element is annotated with given {@code annotation} */
    private Predicate<Key<?>> withTypeName(final String typeNameSuffix)
    {
        return new Predicate<Key<?>>()
        {
            public boolean apply(@Nullable Key<?> input)
            {
                return input != null && (input.getTypeLiteral().getRawType().getName().matches(typeNameSuffix));
            }
        };
    }

    /** where element is annotated with given {@code annotation} */
    private Predicate<Key<?>> withParentType(final Class<?> parentType)
    {
        return new Predicate<Key<?>>()
        {

            public boolean apply(@Nullable Key<?> input)
            {
                boolean value1 = input != null;
                boolean value2 = false;
                if (value1)
                {
                    // Collection<Class<?>> types = new HashSet<Class<?>>();
                    Class<?> süper = input.getTypeLiteral().getRawType().getSuperclass();
                    if (süper != null && süper != Object.class)
                    {
                        value2 = (süper == parentType);
                    }
                    if (!value2)
                    {
                        Class<?>[] interfaces = input.getTypeLiteral().getRawType().getInterfaces();
                        for (Class<?> class1 : interfaces)
                        {
                            value2 = (class1 == parentType);
                            if (value2) break;
                        }
                    }

                }
                return value1 && value2;
            }
        };
    }

    /** where element is annotated with given {@code annotation} */
    private Predicate<Key<?>> withAnnotation(final Class<? extends Annotation> annotation)
    {
        return new Predicate<Key<?>>()
        {

            public boolean apply(@Nullable Key<?> input)
            {
                return input != null && input.getTypeLiteral().getRawType().isAnnotationPresent(annotation);
            }
        };
    }

    /** where element is annotated with given {@code annotation} */
    private Predicate<Key<?>> withAnnotationName(final String annotationNameRegex)
    {
        return new Predicate<Key<?>>()
        {

            public boolean apply(@Nullable Key<?> input)
            {
                boolean value1 = input != null;
                boolean value2 = false;
                if (value1)
                {
                    for (Annotation annotation : input.getTypeLiteral().getRawType().getAnnotations())
                    {
                        if (annotation.annotationType().getName().matches(annotationNameRegex))
                        {
                            value2 = true;
                            break;
                        }
                    }
                }

                // boolean value2 = .getName().endsWith(annotationName);
                return value1 && value2;
            }
        };
    }

    private Function<Key<?>, Class<?>> fromKeyTClass()
    {
        return new Function<Key<?>, Class<?>>()
        {

            @Override
            public Class<?> apply(Key<?> input)
            {
                return input.getTypeLiteral().getRawType();
            }
        };
    }

}
