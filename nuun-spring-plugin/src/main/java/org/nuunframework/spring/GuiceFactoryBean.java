package org.nuunframework.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

import com.google.inject.Injector;

// use this instead http://stackoverflow.com/questions/4540713/add-bean-programatically-to-spring-web-app-context
@Deprecated
public class GuiceFactoryBean implements FactoryBean<Object>{
        
        private Injector injector;
        
        private Class<?> objectType;

        public GuiceFactoryBean(Injector injector, Class<?> objectType) {
                Assert.notNull(injector, "injector required");
                Assert.notNull(objectType, "objectType required");
                this.injector = injector;
                this.objectType = objectType;
        }

        @Override
        public Object getObject() throws Exception {
                return injector.getInstance(objectType);
        }

        @Override
        public Class<?> getObjectType() {
                return objectType;
        }

        @Override
        public boolean isSingleton() {
            // TODO dynamically determine scope.
            
//            BindingScopingVisitor<Object> visitor = new BindingScopingVisitor<Object>()
//            {
//
//                @Override
//                public Object visitEagerSingleton()
//                {
//                    return null;
//                }
//
//                @Override
//                public Object visitScope(Scope scope)
//                {
//                    return null;
//                }
//
//                @Override
//                public Object visitScopeAnnotation(Class<? extends Annotation> scopeAnnotation)
//                {
//                    return null;
//                }
//
//                @Override
//                public Object visitNoScoping()
//                {
//                    return null;
//                }
//            };
//            injector.getBinding(objectType).acceptScopingVisitor(visitor);
                return true;
        }


}