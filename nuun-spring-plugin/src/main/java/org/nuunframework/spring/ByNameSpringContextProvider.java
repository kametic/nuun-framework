package org.nuunframework.spring;

import com.google.inject.Provider;
import org.springframework.beans.factory.BeanFactory;

import static com.google.common.base.Preconditions.checkNotNull;

class ByNameSpringContextProvider<T> implements Provider<T>
{
    final BeanFactory beanFactory;
    final Class<T>    type;
    final String      name;

    public ByNameSpringContextProvider(Class<T> type, String name, BeanFactory beanFactory)
    {
        this.type = checkNotNull(type, "type");
        this.name = checkNotNull(name, "name");
        this.beanFactory = beanFactory;
    }

    public T get()
    {
        return type.cast(beanFactory.getBean(name));
    }
}