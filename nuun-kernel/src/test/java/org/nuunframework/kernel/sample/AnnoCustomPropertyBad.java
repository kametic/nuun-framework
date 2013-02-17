package org.nuunframework.kernel.sample;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.nuunframework.kernel.plugins.configuration.NuunConfigurationConverter;
import org.nuunframework.kernel.plugins.configuration.NuunDummyConverter;
import org.nuunframework.kernel.plugins.configuration.NuunProperty;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@NuunProperty(value = "")
public @interface AnnoCustomPropertyBad
{
    String value();
    boolean mandatory() default true;
    String defaultValue() default "";
    byte defaultByteValue() default 0;
    short defaultShortValue() default 0;
    int defaultIntValue() default 0;
    long defaultLongValue() default 0;
    float defaultFloatValue() default 0;
    double defaultDoubleValue() default 0.0;
    boolean defaultBooleanValue_() default false; // here we add an underscore to brake the equivalence.
    Class<? extends NuunConfigurationConverter<?>> converter() default NuunDummyConverter.class;
}