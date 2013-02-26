package org.nuunframework.kernel.plugin.request;

public enum BindingType
{
    /**
     * Binds the Implementation to itself. Equals binder.bind(Implementation.class);
     */
    IMPLEMENTATION,
    /**
     * Binds the Implementation to all implemented Interfaces. Equals: for(Interface interface:
     * implementedInterfaces) binder.bind(interface).to(implementation);
     */
    INTERFACES,
    /**
     * Binds the Implementation to the extended Super-Class. Equals:
     * binder.bind(superclass).to(implementation);
     */
    SUPER,
    /**
     * Binds the Implementation to the Classes specifed by @To(to={}) Equals: for(Class<?> class: toClasses)
     * binder.bind(class).to(implementation);
     */
    CUSTOM
}