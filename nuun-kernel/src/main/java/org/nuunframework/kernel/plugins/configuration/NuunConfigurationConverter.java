package org.nuunframework.kernel.plugins.configuration;
public interface NuunConfigurationConverter<T> {
     
    /**
     * Converts string value provided by configuration to type expected by
     * annotated field. Each implementation must provide no-argument constructor
     * in order to be instantiated by injector.
     * @param property
     * @return
     */
    T convert(String property);
}
