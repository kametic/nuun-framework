package org.nuunframework.universalvisitor.api;

import java.util.List;

public interface Job<R> {
	
	MapReduce<?>[] mapReduces();
	
	R result(); 

}