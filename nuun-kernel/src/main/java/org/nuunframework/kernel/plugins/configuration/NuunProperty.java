package org.nuunframework.kernel.plugins.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD ,  ElementType.ANNOTATION_TYPE })
public @interface NuunProperty {
    String value();
    boolean mandatory() default true;
    String defaultValue() default "";
    byte defaultByteValue() default 0;
    short defaultShortValue() default 0;
    int defaultIntValue() default 0;
    long defaultLongValue() default 0;
    float defaultFloatValue() default 0;
    double defaultDoubleValue() default 0.0;
    boolean defaultBooleanValue() default false; 
    Class<? extends NuunConfigurationConverter<?>> converter() default NuunDummyConverter.class;
}