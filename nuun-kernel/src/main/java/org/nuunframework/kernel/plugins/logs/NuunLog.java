package org.nuunframework.kernel.plugins.logs;
import javax.inject.Scope;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
 
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
 
@Scope
@Documented
@Retention(RUNTIME)
@Target({FIELD  , ANNOTATION_TYPE })
public @interface NuunLog {
 
}