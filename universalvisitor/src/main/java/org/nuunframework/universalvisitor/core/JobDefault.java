package org.nuunframework.universalvisitor.core;


import org.nuunframework.universalvisitor.api.Job;
import org.nuunframework.universalvisitor.api.MapReduce;
import org.nuunframework.universalvisitor.api.Reducer;

public class JobDefault<R> implements Job<R>{

	private MapReduce<?>[] mapReduces;
	private Reducer<Object, R> resultReducer;

	@SuppressWarnings("rawtypes")
	public JobDefault(MapReduce ...mapReduces  ) {
		this.mapReduces = mapReduces;
	}
	@SuppressWarnings("rawtypes")
	public JobDefault(Reducer<Object, R> resultReducer , MapReduce ...mapReduces  ) {
		this.resultReducer = resultReducer;
		this.mapReduces = mapReduces;
	}
	
	@Override
	public MapReduce<?>[] mapReduces() {
		return mapReduces;
	}

	@Override
	public R result() {
		
		for (MapReduce<?> mapReduce : mapReduces) {
			resultReducer.collect(  mapReduce.aggregate() );
		}
		
		return resultReducer.reduce();
	}

}
