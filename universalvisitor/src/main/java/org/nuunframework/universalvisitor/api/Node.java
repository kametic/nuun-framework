/*
 *  
 */
package org.nuunframework.universalvisitor.api;

import java.lang.reflect.AnnotatedElement;

/**
 *
 * 
 * @author epo.jemba@kametic.com
 *
 */
public interface Node {

	AnnotatedElement annotatedElement();

	Metadata metadata();

	int level();

	Object instance();

}
