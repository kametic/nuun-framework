package org.nuunframework.kernel.commons.function;

class AlwaysFalse<T> implements Predicate<T>
{

    public boolean is(T each, Object... args) {
        return false;
    };
}
