package org.nuunframework.kernel.commons.specification;

import org.apache.commons.lang.ObjectUtils;

public class EqualToSpecification<T> extends AbstractSpecification<T> {
    private final Object value;

    /**
     * Construct a new {@link EqualToSpecification}.
     * @param value the value that our candidates should be equal to
     */
    public EqualToSpecification(Object value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSatisfiedBy(T candidate) {
        return ObjectUtils.equals(candidate, value);
    }

}