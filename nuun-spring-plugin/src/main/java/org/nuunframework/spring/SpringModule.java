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

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.HashMap;
import java.util.Map;

class SpringModule extends AbstractModule
{
    class SpringBeanDefinition
    {
        private String             name;
        private ConfigurableListableBeanFactory beanFactory;

        SpringBeanDefinition(String name, ConfigurableListableBeanFactory beanFactory)
        {
            this.name = name;
            this.beanFactory = beanFactory;
        }

        public String getName()
        {
            return name;
        }

        public ConfigurableListableBeanFactory getBeanFactory()
        {
            return beanFactory;
        }
    }

    private Logger                                           logger = LoggerFactory.getLogger(InternalDependencyInjectionProvider.class);

    private final ConfigurableListableBeanFactory            beanFactory;
    private Map<Class<?>, Map<String, SpringBeanDefinition>> beanDefinitions;

    public SpringModule(ConfigurableListableBeanFactory beanFactory)
    {
        this.beanFactory = beanFactory;
    }

    @Override
    protected void configure()
    {
        this.beanDefinitions = new HashMap<Class<?>, Map<String, SpringBeanDefinition>>();
        bindFromApplicationContext();
    }

    @SuppressWarnings("unchecked")
    private void bindFromApplicationContext()
    {
        boolean debugEnabled = logger.isDebugEnabled();

        ConfigurableListableBeanFactory currentBeanFactory = this.beanFactory;
        do
        {
            for (String beanName : currentBeanFactory.getBeanDefinitionNames())
            {
                BeanDefinition beanDefinition = currentBeanFactory.getMergedBeanDefinition(beanName);
                if (!beanDefinition.isAbstract())
                {
                    Class<?> beanClass = classFromString(beanDefinition.getBeanClassName());
                    if (beanClass == null)
                    {
                        logger.warn("Cannot bind spring bean " + beanName + " because its class " + beanDefinition.getBeanClassName() + " failed to load");
                        return;
                    }

                    SpringBeanDefinition springBeanDefinition = new SpringBeanDefinition(beanName, currentBeanFactory);

                    // Adding bean with its base type
                    addBeanDefinition(beanClass, springBeanDefinition);

                    // Adding bean with its parent type if enabled
                    Class<?> parentClass = beanClass.getSuperclass();
                    if (parentClass != null && parentClass != Object.class)
                        addBeanDefinition(parentClass, springBeanDefinition);

                    // Adding bean with its immediate interfaces if enabled
                    for (Class<?> i : beanClass.getInterfaces())
                        addBeanDefinition(i, springBeanDefinition);
                }
            }
            BeanFactory factory = currentBeanFactory.getParentBeanFactory();
            if (factory != null) {
                if (factory instanceof ConfigurableListableBeanFactory)
                    currentBeanFactory = (ConfigurableListableBeanFactory)factory;
                else {
                    logger.info("Cannot go up further in the bean factory hierarchy, parent bean factory doesn't implement ConfigurableListableBeanFactory");
                    currentBeanFactory = null;
                }
            } else
                currentBeanFactory = null;
        }
        while (currentBeanFactory != null);

        for (Map.Entry<Class<?>, Map<String, SpringBeanDefinition>> entry : this.beanDefinitions.entrySet())
        {
            Class<?> type = entry.getKey();
            Map<String, SpringBeanDefinition> definitions = entry.getValue();

            // Bind by name for each bean of this type and by type if there is no ambiguity
            for (SpringBeanDefinition candidate : definitions.values())
            {
                if (debugEnabled)
                    logger.info("Binding spring bean " + candidate.getName() + " by name and type " + type.getCanonicalName());

                bind(type).annotatedWith(Names.named(candidate.getName())).toProvider(
                        new ByNameSpringContextProvider(type, candidate.getName(), candidate.getBeanFactory()));
            }
        }
    }

    private void addBeanDefinition(Class<?> beanClass, SpringBeanDefinition springBeanDefinition)
    {
        Map<String, SpringBeanDefinition> beansOfType = this.beanDefinitions.get(beanClass);
        if (beansOfType == null)
            this.beanDefinitions.put(beanClass, beansOfType = new HashMap<String, SpringBeanDefinition>());

        if (!beansOfType.containsKey(springBeanDefinition.getName())) // TODO determine which beans override which in spring semantics ? Here first discovered wins...
            beansOfType.put(springBeanDefinition.getName(), springBeanDefinition);
    }

    private Class<?> classFromString(String className)
    {
        try
        {
            return Class.forName(className);
        }
        catch (ClassNotFoundException e)
        {
            return null;
        }
    }

}
