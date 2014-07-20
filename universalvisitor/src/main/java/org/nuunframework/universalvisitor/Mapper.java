package org.nuunframework.universalvisitor;

/**
 *
 * 
 * @author Epo Jemba
 * @author Pierre Thirouin
 *
 */
public interface Mapper<T> {
	
	T map (Node node);

}
