
package org.nuunframework.kernel.tests.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.nuunframework.kernel.plugin.Plugin;

/**
 * This annotation can be used on integration tests to specify manually which plugins SEED will load.
 *
 * @author epo.jemba@kametic.com
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface WithPlugins {

    Class<? extends Plugin>[] value();

}
