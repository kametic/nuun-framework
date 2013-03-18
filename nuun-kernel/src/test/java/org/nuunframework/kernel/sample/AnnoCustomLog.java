package org.nuunframework.kernel.sample;
import javax.inject.Scope;

import org.nuunframework.kernel.plugins.logs.NuunLog;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
 
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
 
@Scope
@Documented
@Retention(RUNTIME)
@Target({FIELD  })
@NuunLog
public @interface AnnoCustomLog {
 
}