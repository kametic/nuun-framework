/*
*   _________________________________
*   ))                              (( 
*  ((   __    o     ___        _     ))
*   ))  ))\   _   __  ))   __  ))   (( 
*  ((  ((_/  ((  ((- ((__ ((- ((     ))
*   ))        )) ((__     ((__ ))__  (( 
*  ((                                ))
*   ))______________________________(( 
*        Diezel 2.0.0 Generated.
*
*/
package org.nuunframework.kernel.api.topology.grapher ;

import org.nuunframework.kernel.commons.specification.Specification;

/**
*
*/
public interface Grapher {
    /**
    **/
    public  Grapher3  newInstance(String name, Class<?> type) ;/**
    **/
    public  Grapher2  newReference(String name) ;/**
    **/
    public  Grapher3  newInstance(String name) ;/**
    **/
    public  Grapher3  newSpecificationInstance(String name,Specification<Class<?>> specification) ;/**
    **/
    public  Grapher6  newCompositeInstance(String name) ;
}