package org.nuunframework.spring;

import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.ApplicationContext;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
@SuppressWarnings({
    "unchecked", "rawtypes"
})
class SpringModule extends AbstractModule
{
    
    private final ApplicationContext applicationContext;
    private Set<Class<?>> mappedClasses;
    private final boolean byName;
    private final boolean byInterfaces;
    private final boolean byParent;
    
    
    public SpringModule(ApplicationContext context)
    {
        this(context,true,false,false);
    }

    public SpringModule(ApplicationContext context,boolean byName, boolean byInterfaces )
    {
        this(context,byName,byInterfaces,false);
    }
    
    
    public SpringModule(ApplicationContext context, boolean byName, boolean byInterfaces , boolean byParent )
    {
        this.applicationContext = context;
        this.byName = byName;
        this.byInterfaces = byInterfaces;
        this.byParent = byParent;
        
    }
    
    @Override
    protected void configure()
    {
        mappedClasses = new HashSet<Class<?>>();
        bindFromApplicationContext();
    }
    
    
    private void bindFromApplicationContext() {
        for (String beanName : this.applicationContext.getBeanDefinitionNames()) {
            Object bean = this.applicationContext.getBean(beanName);
            Class<?> type = this.applicationContext.getType(beanName);
            if(bean != null) {
                if (byName)
                {
                    bindByName (beanName , type );
                }
                if (byInterfaces)
                {
                    bindInterfaces(bean);
                }
                if (byParent) 
                {
                    bindParent (bean);
                }
                
            }
        }
    }
    
    private void bindParent(Object bean)
    {
        final Class clazz = bean.getClass().getSuperclass();
        if (clazz != null  && clazz != Object.class)
        {
            bindType(clazz);
        }
    }

    void bindByName(String name, Class type) {
          ByNameSpringContextProvider provider = ByNameSpringContextProvider.newInstance(type, name);
          try {
            provider.initialize(this.applicationContext);
          }
          catch (Exception e) {
            addError(e);
            return;
          }

          bind(type).annotatedWith(Names.named(name)) .toProvider(provider);
        }

    private void bindType(Class clazz)
    {
        //Check to see if we have already bound a provider for this interface.  If we have,
        //then don't attempt to bind it again as the provider will pull from the application context by type
        //and any multi-bean errors will be handled at injection time
        if(!mappedClasses.contains(clazz)) {
            bind(clazz).toProvider(new ByTypeSpringContextProvider(clazz, this.applicationContext));
            mappedClasses.add(clazz);
        }        
    }
    

    private void bindInterfaces(Object bean) {
        String fullClassName = Proxy.isProxyClass(bean.getClass()) ? bean.toString() : bean.getClass().getName();
        if (isAddable(fullClassName)) {
            for (final Class clazz : bean.getClass().getInterfaces()) {
                bindType(clazz);
            }
        }
    }
    
    private boolean isAddable(String fullClassName) {
        return true;
//        for(String basePackage : basePackages) {
//            if(fullClassName.matches(basePackage + ".*")) {
//                return true;
//            }
//        }
//        return false;
    }

}
