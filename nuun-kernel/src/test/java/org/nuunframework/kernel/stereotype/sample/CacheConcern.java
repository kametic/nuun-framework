package org.nuunframework.kernel.stereotype.sample;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.nuunframework.kernel.stereotype.Concern;

@Concern(name="cache" , priority=Concern.Priority.HIGHEST , order=-2)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE})
public @interface CacheConcern
{
}
