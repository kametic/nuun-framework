package org.nuunframework.kernel.stereotype.sample;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import org.nuunframework.kernel.stereotype.Concern;

@Concern(name="security" , priority=Concern.Priority.LOW)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE})
public @interface LogConcern
{
}
