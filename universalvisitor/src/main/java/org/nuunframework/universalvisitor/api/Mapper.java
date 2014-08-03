package org.nuunframework.universalvisitor.api;

import java.lang.reflect.AnnotatedElement;

/**
 *
 * 
 * 
 * @author Epo Jemba
 * @author Pierre Thirouin
 *
 */
public interface Mapper<O> {
	
	boolean handle (AnnotatedElement object);
	
	O map (Node node);

}
