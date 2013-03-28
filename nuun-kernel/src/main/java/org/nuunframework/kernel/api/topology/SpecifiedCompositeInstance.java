package org.nuunframework.kernel.api.topology;

import org.nuunframework.kernel.commons.specification.Specification;

public interface SpecifiedCompositeInstance extends CompositeInstance 
{
	
	Specification<Instance> specification();

}
