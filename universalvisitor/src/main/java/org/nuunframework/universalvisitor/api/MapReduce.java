package org.nuunframework.universalvisitor.api;


public interface MapReduce<T> {
	
	public Mapper<T> getMapper();

	public Reducer<T, ?>[] getReducers();
	
	public Object aggregate ();
	
}
