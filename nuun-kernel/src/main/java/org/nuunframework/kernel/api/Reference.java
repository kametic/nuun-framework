package org.nuunframework.kernel.api;

public interface Reference
{
    String name ();
    Instance source();
    Instance target();
    boolean optionnal();
}
