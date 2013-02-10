package org.nuunframework.kernel.commons.specification;

import org.apache.commons.lang.ObjectUtils;

public class GreaterThanSpecification<T extends Comparable<T>> extends CompareToSpecification<T> implements SubsumableSpecification<T> {

    /**
     * Construct a new {@link GreaterThanSpecification}.
     * @param value value that candidates need to exceed
     */
    public GreaterThanSpecification(T value) {
        super(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isSatisfyingComparison(int comparison) {
        return comparison > 0; // Positive integers are considered a greater than comparison
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean subsumes(Specification<T> other) {
        if (other instanceof GreaterThanSpecification<?>) {
            // Other greater-than specifications are considered 'subsumed' whenever their value is equal or lower
            // Whenever a value is greater than, for example '2', it will always be greater than anything smaller
            return ObjectUtils.compare(getValue(), ((GreaterThanSpecification<T>) other).getValue()) >= 0;
        }
        return false;
    }

}