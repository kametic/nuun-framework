package org.nuunframework.kernel.api;


/**
 * 
 * This type hold a definition of an ObjectGraph. It will generate an ObjectGraph.
 * 
 * @author ejemba
 *
 */
public interface ObjectGraphDef
{
    
    String name();
    ObjectGraph generate ();
      
}
