package org.nuunframework.kernel.tests.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used on integration tests to mark a class as being expected.
 * <p>
 * The class will be in major cases an Exception, an Error or a Throwable. But it can 
 * be anything expected.
 * 
 * @author epo.jemba@kametic.com
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Expect {
    Class<?> value();
}
