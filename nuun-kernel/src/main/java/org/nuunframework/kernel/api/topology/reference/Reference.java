package org.nuunframework.kernel.api.topology.reference;

import org.nuunframework.kernel.api.topology.TopologyElement;
import org.nuunframework.kernel.api.topology.instance.Instance;

public interface Reference extends TopologyElement
{
    Instance source();
    Instance target();
    boolean optionnal();
}
