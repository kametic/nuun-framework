package org.nuunframework.universalvisitor.api;

/**
 * 
 * @author Epo Jemba
 * @author Pierre Thirouin
 *
 */
public interface Reducer <T,R>{
	
	void collect (T input);
	
	R reduce();

}
