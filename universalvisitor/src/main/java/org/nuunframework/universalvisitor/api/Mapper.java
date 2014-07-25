package org.nuunframework.universalvisitor.api;

import java.lang.reflect.AccessibleObject;

/**
 *
 * 
 * 
 * @author Epo Jemba
 * @author Pierre Thirouin
 *
 */
public interface Mapper<O> {
	
	boolean handle (AccessibleObject object);
	
	O map (Node node);

}
