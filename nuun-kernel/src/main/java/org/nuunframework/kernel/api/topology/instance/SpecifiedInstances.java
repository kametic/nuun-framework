package org.nuunframework.kernel.api.topology.instance;

import org.nuunframework.kernel.commons.specification.Specification;

public interface SpecifiedInstances extends CompositeInstance 
{
	
	Specification<Instance> specification();

}
