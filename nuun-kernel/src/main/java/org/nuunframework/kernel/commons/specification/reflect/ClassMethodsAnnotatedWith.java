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
				logger.warn("Exception on isSatisfiedBy () " + cnfe.getMessage());
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
