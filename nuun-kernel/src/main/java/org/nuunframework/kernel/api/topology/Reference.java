package org.nuunframework.kernel.api.topology;

public interface Reference
{
    String name ();
    Instance source();
    Instance target();
    boolean optionnal();
}
