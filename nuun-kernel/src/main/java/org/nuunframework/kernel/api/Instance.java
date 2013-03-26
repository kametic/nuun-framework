package org.nuunframework.kernel.api;

import java.util.Collection;

import org.nuunframework.kernel.commons.specification.Specification;

public interface Instance
{
    /**
     * @return The name of the instance
     */
    String name();
    
    /**
     * @return the type of the instance
     */
    Class<?> type();
    
    /**
     * When instance is a wildcard the specification to
     * reach the set 
     * 
     * @return
     */
    Specification<Class<?>> typeSubset();
    
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

    /**
     * Meta information describing the Instance 
     * 
     * @return can be Nominal or Wildcard ( an instance that represent user)
     */
    InstanceMeta meta();
}
