package org.nuunframework.kernel.plugins.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Class that is annotated by this annotation must have one method that matches :
 * 
 *          T convert(String property);
 * 
 * @author ejemba
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE ,  ElementType.ANNOTATION_TYPE })
public @interface NuunPropertyConverter { 
}