package org.nuunframework.universalvisitor.api;


public class MapReduce<T>  {

	
	private Mapper<T> mapper;
	private Reducer<T, ?>[] reducers;

	public MapReduce(Mapper<T> mapper , Reducer<T,?> ...reducers) {
		this.mapper = mapper;
		this.reducers = reducers;
	}

	public Mapper<T> getMapper() {
		return mapper;
	}

	public Reducer<T, ?>[] getReducers() {
		return reducers;
	}
	
}
