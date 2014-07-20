package org.nuunframework.universalvisitor;

/**
 *
 * 
 * @author Epo Jemba
 * @author Pierre Thirouin
 *
 */
public interface Reducer <T,R>{
	
	void collect (T input);
	
	R reduce();

}
