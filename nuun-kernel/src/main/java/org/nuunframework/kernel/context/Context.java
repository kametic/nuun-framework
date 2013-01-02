/**
 * 
 */
package org.nuunframework.kernel.context;

import java.lang.annotation.Annotation;
import java.util.Collection;


/**
 * @author Epo Jemba
 *
 */
public interface Context
{
    
    Collection<Class<?>> getClassAnnotatedWith(Class<? extends Annotation> clazz);
    
    Collection<Class<?>> getClassAnnotatedWithRegex(String name);
    
    Collection<Class<?>> getClassWithParentType(Class<?> parentClass);
    
    Collection<Class<?>> getClassTypeByRegex(String typeNameSuffix);

}
