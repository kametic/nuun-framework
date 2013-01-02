package org.nuunframework.kernel.commons.function;

class AlwaysTrue<T> implements Predicate<T>
{
    public boolean is(T each, Object... args) {
        return true;
    };
}
