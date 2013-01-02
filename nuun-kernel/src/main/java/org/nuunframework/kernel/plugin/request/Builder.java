/**
 * 
 */
package org.nuunframework.kernel.plugin.request;


/**
 * @author Epo Jemba
 *
 */
public interface Builder<T>
{
    T build ();
    
    void reset();
}
