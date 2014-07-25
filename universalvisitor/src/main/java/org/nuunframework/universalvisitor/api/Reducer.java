package org.nuunframework.universalvisitor.api;

/**
 * 
 * @author Epo Jemba
 * @author Pierre Thirouin
 *
 */
public interface Reducer <I,O>{
	
	void collect (I input);
	
	O reduce();

}
