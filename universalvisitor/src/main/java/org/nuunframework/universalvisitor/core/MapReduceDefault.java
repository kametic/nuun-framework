package org.nuunframework.universalvisitor.core;

import org.nuunframework.universalvisitor.api.MapReduce;
import org.nuunframework.universalvisitor.api.Mapper;
import org.nuunframework.universalvisitor.api.Reducer;

public class MapReduceDefault<T> implements MapReduce<T>{
	
	private Mapper<T> mapper;
	private Reducer<T, ?>[] reducers;

	public MapReduceDefault(Mapper<T> mapper , Reducer<T,?> ...reducers) {
		this.mapper = mapper;
		this.reducers = reducers;
	}

	public Mapper<T> getMapper() {
		return mapper;
	}

	public Reducer<T, ?>[] getReducers() {
		return reducers;
	}

	@Override
	public Object aggregate() {
		return null;
	}
	
}
