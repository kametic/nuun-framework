package org.nuunframework.universalvisitor;

import java.lang.reflect.AccessibleObject;

/**
 *
 * 
 * 
 * @author Epo Jemba
 * @author Pierre Thirouin
 *
 */
public interface Mapper<T> {
	
	boolean handle (AccessibleObject object);
	
	T map (Node node);

}
