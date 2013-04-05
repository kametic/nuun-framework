package org.nuunframework.kernel.api.topology.instance;

import java.util.Collection;

/**
 * 
 * A typed instance is an instance with a Type.
 * 
 * @author Epo Jemba
 *
 */
public interface TypedInstance extends Instance {

    /**
     * @return the type of the instance
     */
    Class<?> type();
    

	
}
