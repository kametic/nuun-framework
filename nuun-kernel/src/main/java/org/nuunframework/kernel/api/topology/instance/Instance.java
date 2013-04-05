package org.nuunframework.kernel.api.topology.instance;

import java.util.Collection;

import org.nuunframework.kernel.api.topology.TopologyElement;
import org.nuunframework.kernel.api.topology.reference.Reference;

public interface Instance extends TopologyElement
{

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
