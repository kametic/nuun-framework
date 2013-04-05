package org.nuunframework.kernel.api.topology.instance;

import java.util.Collection;

public interface CompositeInstance extends Instance {
	
	Collection<Instance> instances();

}
