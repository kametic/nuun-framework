package org.nuunframework.spring;

import java.util.Map;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;

import com.google.inject.Provider;

/**
 * Provides beans for injection by Guice that are managed by Spring
 * @param <T> the type of the bean to pull
 */
class ByTypeSpringContextProvider<T> implements Provider<T> {

    private final Class<T> clazz;
    private final ApplicationContext context;

    public ByTypeSpringContextProvider(Class<T> clazz, ApplicationContext context) {
        this.clazz = clazz;
        this.context = context;
    }

    @Override
    public T get() {
        Map<String, T> beans = context.getBeansOfType(clazz);
        return beans.size() == 1 ? getSingleBean(beans) : getPrimaryBean(beans);
    }

    private T getPrimaryBean(Map<String, T> beans) {
        AutowireCapableBeanFactory factory = context.getAutowireCapableBeanFactory();
        for(Map.Entry<String, T> bean : beans.entrySet()) {
            if(factory instanceof ConfigurableListableBeanFactory &&
                    ((ConfigurableListableBeanFactory) factory).getBeanDefinition(bean.getKey()).isPrimary()) {
                return bean.getValue();
            }
        }
        throw new NoSuchBeanDefinitionException(clazz, "Matching bean count for class: " + beans.size());
    }

    private T getSingleBean(Map<String, T> beans) {
        return beans.entrySet().iterator().next().getValue();
    }
}