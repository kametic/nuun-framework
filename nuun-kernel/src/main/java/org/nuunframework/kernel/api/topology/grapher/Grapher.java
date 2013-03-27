package org.nuunframework.kernel.api.topology.grapher;

import org.nuunframework.kernel.commons.specification.Specification;



public interface Grapher
{
    /**
     * Declaring a new instance to be part of the Graph 
     *  
     *  @param name The name of the instance
     *  @param type the type of the instance
     * @return
     */
    InstanceBuilder newInstance ( String name , Class<?> type );
    
    InstanceBuilder newInstance ( String name , Specification<Class<?>> typeSubset );
    
    /**
     * 
     * Declaring a new reference to be part of the graph
     * 
     * @param referenceName the name of the reference. Has to be unique among all objects graph.
     * @return
     */
    ReferenceBuilder newReference(String referenceName); 
    
    
    
    
}
