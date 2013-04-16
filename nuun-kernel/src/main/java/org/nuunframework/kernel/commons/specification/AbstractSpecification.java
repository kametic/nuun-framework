package org.nuunframework.kernel.commons.specification;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public abstract class AbstractSpecification<T> implements Specification<T>
{

    /**
     * do not remove this simple object insure the specification is unique
     */
    private Object id = new Object();
    
    // Chaining

    /**
     * {@inheritDoc}
     */
    @Override
    public AndSpecification<T> and(Specification<? super T> rhs)
    {
        return new AndSpecification<T>(this, rhs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrSpecification<T> or(Specification<? super T> rhs)
    {
        return new OrSpecification<T>(this, rhs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NotSpecification<T> not()
    {
        return new NotSpecification<T>(this);
    }

    // Object behaviour

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj)
    {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public Object getId()
    {
        return id;
    }

    public void setId(Object id)
    {
        this.id = id;
    }

}