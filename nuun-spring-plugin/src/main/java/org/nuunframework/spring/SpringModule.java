package org.nuunframework.spring;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
import java.util.Map;

class SpringModule extends AbstractModule {
    class SpringBeanDefinition {
        private String name;
        private ApplicationContext context;

        SpringBeanDefinition(String name, ApplicationContext context) {
            this.name = name;
            this.context = context;
        }

        public String getName() {
            return name;
        }

        public ApplicationContext getContext() {
            return context;
        }
    }

    private Logger logger = LoggerFactory.getLogger(InternalDependencyInjectionProvider.class);

    private final ApplicationContext applicationContext;
    private Map<Class<?>, Map<String, SpringBeanDefinition>> beanDefinitions;

    public SpringModule(ApplicationContext context) {
        this.applicationContext = context;
    }

    @Override
    protected void configure() {
        this.beanDefinitions = new HashMap<Class<?>, Map<String, SpringBeanDefinition>>();
        bindFromApplicationContext();
    }

    @SuppressWarnings("unchecked")
    private void bindFromApplicationContext() {
        ApplicationContext currentApplicationContext = this.applicationContext;
        boolean debugEnabled = logger.isDebugEnabled();

        do {
            if (!(currentApplicationContext instanceof ConfigurableApplicationContext)) {
                logger.warn("Spring application context " + currentApplicationContext.getDisplayName() + " is not assignable to AbstractBeanFactory, spring beans will not be available");
                continue;
            }

            ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) currentApplicationContext).getBeanFactory();
            if (beanFactory == null) {
                logger.warn("Spring application context " + currentApplicationContext.getDisplayName() + " doesn't have a bean factory, spring beans will not be available");
                continue;
            }

            for (String beanName : currentApplicationContext.getBeanDefinitionNames()) {
                BeanDefinition beanDefinition = beanFactory.getMergedBeanDefinition(beanName);
                if (!beanDefinition.isAbstract()) {
                    Class<?> beanClass = classFromString(beanDefinition.getBeanClassName());
                    if (beanClass == null) {
                        logger.warn("Cannot bind spring bean " + beanName + " because its class " + beanDefinition.getBeanClassName() + " failed to load");
                        return;
                    }

                    SpringBeanDefinition springBeanDefinition = new SpringBeanDefinition(beanName, currentApplicationContext);

                    // Adding bean with its base type
                    addBeanDefinition(beanClass, springBeanDefinition);

                    // Adding bean with its parent type if enabled
                    Class<?> parentClass = beanClass.getSuperclass();
                    if (parentClass != null && parentClass != Object.class)
                        addBeanDefinition(parentClass, springBeanDefinition);

                    // Adding bean with its immediate interfaces
                    for (Class<?> i : beanClass.getInterfaces())
                        addBeanDefinition(i, springBeanDefinition);
                }
            }
            currentApplicationContext = currentApplicationContext.getParent();
        } while (currentApplicationContext != null);

        for (Map.Entry<Class<?>, Map<String, SpringBeanDefinition>> entry : this.beanDefinitions.entrySet()) {
            Class<?> type = entry.getKey();
            Map<String, SpringBeanDefinition> definitions = entry.getValue();

            // Bind by name for each bean of this type
            boolean alreadyBoundByType = false;
            for (SpringBeanDefinition candidate : definitions.values()) {
                if (!alreadyBoundByType) {
                    if (debugEnabled)
                        logger.debug("Binding spring bean " + candidate.getName() + " by type " + type.getCanonicalName());

                    bind(type).toProvider(new ByTypeSpringContextProvider(type, candidate.getContext()));
                    alreadyBoundByType = true;
                }

                if (debugEnabled)
                    logger.debug("Binding spring bean " + candidate.getName() + " by name and type " + type.getCanonicalName());

                bind(type).annotatedWith(Names.named(candidate.getName())).toProvider(new ByNameSpringContextProvider(type, candidate.getName(), candidate.getContext()));
            }
        }
    }

    private void addBeanDefinition(Class<?> beanClass, SpringBeanDefinition springBeanDefinition) {
        Map<String, SpringBeanDefinition> beansOfType = this.beanDefinitions.get(beanClass);
        if (beansOfType == null)
            this.beanDefinitions.put(beanClass, beansOfType = new HashMap<String, SpringBeanDefinition>());

        if (!beansOfType.containsKey(springBeanDefinition.getName())) // TODO determine which beans override which in spring semantics ? Here first discovered wins...
            beansOfType.put(springBeanDefinition.getName(), springBeanDefinition);
    }


    private Class<?> classFromString(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
