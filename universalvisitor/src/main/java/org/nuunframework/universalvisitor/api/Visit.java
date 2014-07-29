package org.nuunframework.universalvisitor.api;

import java.util.List;


public interface Visit <R>{
	
    List<MapReduce<?>> mapReduceList ();

    R report (MapReduce<?> map);

}
