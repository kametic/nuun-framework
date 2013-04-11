package org.nuunframework.kernel.api.topology.objectgraph;

import java.util.Collection;

import org.nuunframework.kernel.api.topology.instance.CompositeInstance;
import org.nuunframework.kernel.api.topology.instance.Instance;
import org.nuunframework.kernel.api.topology.instance.SpecifiedInstances;
import org.nuunframework.kernel.api.topology.instance.TypedInstance;
import org.nuunframework.kernel.api.topology.reference.Reference;

/**
 * an ObjectGraph hold information between Instances thanks to ref.
 * 
 * 
 * @author ejemba
 */
public interface ObjectGraph
{
    Collection<Instance> instances();
    Collection<Reference> references();
    Instance instance(String name);
    Collection<Instance> instancesByRegex(String regex);
    
    
    Reference reference(String name);    
    Collection<Reference> referencesByTargetType(Class<?> klass);
    Collection<Reference> referencesByRegex(String regex);
    Collection<Reference> referencesTargetAssignableFrom(Class<?> klass);
    
    Collection<TypedInstance> instancesByClass(Class<?> klass);
    Collection<TypedInstance> instancesAssignableFrom(Class<?> klass);

    Collection<CompositeInstance> compositeInstances();
    Collection<SpecifiedInstances> specifiedInstances();
}
