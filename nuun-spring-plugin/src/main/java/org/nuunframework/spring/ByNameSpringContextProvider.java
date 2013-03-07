package org.nuunframework.spring;

import org.springframework.beans.factory.BeanFactory;

import com.google.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

class ByNameSpringContextProvider<T> implements Provider<T> {

    BeanFactory beanFactory;
    boolean singleton;
    final Class<T> type;
    final String name;

    public ByNameSpringContextProvider(Class<T> type, String name) {
      this.type = checkNotNull(type, "type");
      this.name = checkNotNull(name, "name");
    }

    static <T> ByNameSpringContextProvider<T> newInstance(Class<T> type, String name) {
      return new ByNameSpringContextProvider<T>(type, name);
    }

    void initialize(BeanFactory beanFactory) {
      this.beanFactory = beanFactory;
      if (!beanFactory.isTypeMatch(name, type)) {
        throw new ClassCastException("Spring bean named '" + name
            + "' does not implement " + type.getName() + ".");
      }
      singleton = beanFactory.isSingleton(name);
    }

    public T get() {
      return singleton ? getSingleton() : type.cast(beanFactory.getBean(name));
    }

    volatile T instance;

    private T getSingleton() {
      if (instance == null) {
        instance = type.cast(beanFactory.getBean(name));
      }
      return instance;
    }
  }