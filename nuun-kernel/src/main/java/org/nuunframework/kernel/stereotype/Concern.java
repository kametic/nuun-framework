package org.nuunframework.kernel.stereotype;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({
    ElementType.ANNOTATION_TYPE
}) 
public @interface Concern
{

    int order() default 0;
    String name();
    Priority priority() default Priority.NORMAL;
    public enum Priority
    {
        LOWEST , LOWER , LOW , NORMAL , HIGH , HIGHER , HIGHEST
    }
    
}
