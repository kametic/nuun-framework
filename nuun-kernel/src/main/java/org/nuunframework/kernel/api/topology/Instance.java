package org.nuunframework.kernel.api.topology;

import java.util.Collection;

public interface Instance
{
    /**
     * @return The name of the instance
     */
    Object identifier();
    
    /**
     * @return The name of the instance
     */
    String description();
    
    
    /**
     * @return the reference associated with the instance.
     */
    Collection<Reference> references();
    /**
     * 
     * @param klass the klass 
     * 
     * @return the reference where target type is assignable with klass.
     */
    Collection<Reference> referencesAssignableFrom(Class<?> klass);
    
    /**
     * 
     * @param regex
     * 
     * @return references where name matches regex. 
     */
    Collection<Reference> referencesByRegex(String regex);

}
