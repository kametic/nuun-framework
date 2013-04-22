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
package org.nuunframework.spring;

import com.google.inject.Provider;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Provides beans for injection by Guice that are managed by Spring
 * 
 * @param <T>
 *            the type of the bean to pull
 */
class ByTypeSpringContextProvider<T> implements Provider<T>
{

    private final Class<T>           clazz;
    private final ApplicationContext context;

    public ByTypeSpringContextProvider(Class<T> clazz, ApplicationContext context)
    {
        this.clazz = clazz;
        this.context = context;
    }

    @Override
    public T get()
    {
        Map<String, T> beans = context.getBeansOfType(clazz);
        return beans.size() == 1 ? getSingleBean(beans) : getPrimaryBean(beans);
    }

    private T getPrimaryBean(Map<String, T> beans)
    {
        AutowireCapableBeanFactory factory = context.getAutowireCapableBeanFactory();
        for (Map.Entry<String, T> bean : beans.entrySet())
        {
            if (factory instanceof ConfigurableListableBeanFactory && ((ConfigurableListableBeanFactory) factory).getBeanDefinition(bean.getKey()).isPrimary())
            {
                return bean.getValue();
            }
        }
        throw new NoSuchBeanDefinitionException(clazz, "Matching bean count for class: " + beans.size());
    }

    private T getSingleBean(Map<String, T> beans)
    {
        return beans.entrySet().iterator().next().getValue();
    }
}