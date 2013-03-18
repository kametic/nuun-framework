package org.nuunframework.kernel.commons.specification.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.nuunframework.kernel.commons.specification.AbstractSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassMethodsAnnotatedWith extends AbstractSpecification<Class<?>> 
{
	
	Logger logger = LoggerFactory.getLogger(ClassMethodsAnnotatedWith.class);
	
	private MethodAnnotatedWith methodAnnotatedWith;
	
	public ClassMethodsAnnotatedWith(Class<? extends Annotation> annotation) 
	{
		methodAnnotatedWith = new MethodAnnotatedWith(annotation);
	}
	
	@Override
	public boolean isSatisfiedBy(Class<?> candidate) 
	{
		if (null != candidate)
		{
			try {
				
				Class<?>[] clazzes = getAllInterfacesAndClasses(candidate);
				
				for(Class<?> clazz : clazzes) 
				{
					for ( Method method : clazz.getDeclaredMethods() )
					 {
						 if ( methodAnnotatedWith.isSatisfiedBy(method) ) 
						 {
							 return true;
						 }
					 }
				 }
				
			} 
			  catch (Throwable cnfe)
			{
				logger.warn("Exception on isSatisfiedBy () ", cnfe);
			}
		}
		
		return false;
	}
	
	
    Class<?>[] getAllInterfacesAndClasses(Class<?> clazz) {
        return getAllInterfacesAndClasses(new Class[] { clazz } );
    }
	
	 //This method walks up the inheritance hierarchy to make sure we get every class/interface that could
    //possibly contain the declaration of the annotated method we're looking for.
    @SuppressWarnings("unchecked")
	 Class<?>[] getAllInterfacesAndClasses ( Class<?>[] classes ) 
    {
        if(0 == classes.length ) 
        {
            return classes;
        }
        else 
        {
            List<Class<?>> extendedClasses = new ArrayList<Class<?>>();
            // all interfaces hierarchy
            for (Class<?> clazz: classes) {
            	if (clazz != null) 
            	{
            		Class<?>[] interfaces = clazz.getInterfaces();
	                if (interfaces != null)
	                {
	                	extendedClasses.addAll((List<? extends Class<?>>) Arrays.asList( interfaces ) );
	                }
	                Class<?> superclass = clazz.getSuperclass();
	                if (superclass != null && superclass != Object.class)
	                {
	                	extendedClasses.addAll((List<? extends Class<?>>) Arrays.asList( superclass ) );
	                }
            	}
            }
            
            //Class::getInterfaces() gets only interfaces/classes implemented/extended directly by a given class.
            //We need to walk the whole way up the tree.
            return (Class[]) ArrayUtils.addAll( classes, getAllInterfacesAndClasses( extendedClasses.toArray(new Class[extendedClasses.size()])));
        }
    }
	
	
}
