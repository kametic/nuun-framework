package org.nuunframework.universalvisitor.api;


public interface Job<R> {
	
	MapReduce<?>[] mapReduces();
	
	R result();

}