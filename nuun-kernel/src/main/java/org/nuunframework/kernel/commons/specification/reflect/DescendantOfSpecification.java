package org.nuunframework.kernel.commons.specification.reflect;

import org.nuunframework.kernel.commons.specification.AbstractSpecification;
/**
 * 
 * Strictly descendant of a candidate class.
 * 
 * @author ejemba
 *
 */
public class DescendantOfSpecification extends AbstractSpecification<Class<?>>
{

    private Class<?> ancestorType;

    public DescendantOfSpecification(Class<?> ancestorType)
    {
        this.ancestorType = ancestorType;
    }
    
    @Override
    public boolean isSatisfiedBy(Class<?> candidate)
    {
        return candidate != null &&  candidate != ancestorType && ancestorType.isAssignableFrom(candidate);
    }

}
