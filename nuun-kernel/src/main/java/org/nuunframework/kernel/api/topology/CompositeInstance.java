package org.nuunframework.kernel.api.topology;

import java.util.Collection;

public interface CompositeInstance extends Instance {
	
	Collection<Instance> instances();

}
