/*
 *  
 */
package org.nuunframework.universalvisitor.api;

import java.lang.reflect.AccessibleObject;

/**
 *
 * 
 * @author epo.jemba@kametic.com
 *
 */
public interface Node {

	AccessibleObject accessibleObject();

	Metadata metadata();

	int level();

	Object instance();

}
