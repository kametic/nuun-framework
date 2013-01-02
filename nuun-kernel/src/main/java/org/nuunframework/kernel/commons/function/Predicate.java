package org.nuunframework.kernel.commons.function;

@SuppressWarnings("rawtypes")
public interface Predicate<T>
{

    boolean is(T each, Object... args);
    
    public static Predicate TRUE = new AlwaysTrue<Object>();
    public static Predicate FALSE = new AlwaysFalse();

}