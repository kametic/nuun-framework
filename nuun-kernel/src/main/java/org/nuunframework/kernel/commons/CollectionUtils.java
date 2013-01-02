/**
 * 
 */
package org.nuunframework.kernel.commons;

import java.util.Collection;


/**
 * @author Epo Jemba
 *
 */
public class CollectionUtils
{

    private CollectionUtils() 
    {        
    }
    
//    public static <T> Collection<T> convertToCollection (T... items)
//    {
//        return Arrays.asList(items);
//    }

    @SuppressWarnings("unchecked")
    public static <T> T[]  convertToArray(Collection<T> items)
    {
        return (T[]) items.toArray();
    }
    
}
