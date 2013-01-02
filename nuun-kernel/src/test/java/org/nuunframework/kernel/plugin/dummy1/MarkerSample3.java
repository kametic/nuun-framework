package org.nuunframework.kernel.plugin.dummy1;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;



@Retention(RetentionPolicy.RUNTIME)
@Target({  ElementType.TYPE , ElementType.FIELD})
@Qualifier
public @interface MarkerSample3
{

}
