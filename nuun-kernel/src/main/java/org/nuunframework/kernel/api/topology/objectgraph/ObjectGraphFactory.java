package org.nuunframework.kernel.api.topology.objectgraph;


/**
 * 
 * This type hold a definition of an ObjectGraph. It will generate an ObjectGraph.
 * 
 * @author ejemba
 *
 */
public interface ObjectGraphFactory
{
    
    String name();
    ObjectGraph generate ();
      
}
