package org.nuunframework.kernel.api.topology;

import java.util.Collection;

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
    Reference reference(String name);
    
    Collection<Reference> referencesByTargetType(Class<?> klass);
    Collection<Reference> referencesByRegex(String regex);
    Collection<Reference> referencesTargetAssignableFrom(Class<?> klass);
    
    Collection<Instance> instancesAssignableFrom(Class<?> klass);
    Collection<Instance> instancesAsWildcard();
    Collection<Instance> instancesByClass(Class<?> klass);
    Collection<Instance> instancesByRegex(String regex);

}
