package org.nuunframework.kernel.commons.dsl;

import java.util.List;

public interface CompositeDSLNode extends DSLNode {

	List<DSLNode> siblingNodes();

}