package org.nuunframework.kernel.commons.specification;

import org.apache.commons.lang.ObjectUtils;

public class LessThanSpecification<T extends Comparable<T>> extends CompareToSpecification<T> implements SubsumableSpecification<T> {

    /**
     * Construct a new {@link LessThanSpecification}.
     * @param value value that candidates need to stay below
     */
    public LessThanSpecification(T value) {
        super(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isSatisfyingComparison(int comparison) {
        return comparison < 0; // Negative integers are considered a lesser than comparison
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean subsumes(Specification<T> other) {
        if (other instanceof LessThanSpecification<?>) {
            // Other less-than specifications are considered 'subsumed' whenever their value is equal or higher
            // Whenever a value is less than, for example '2', it will always be less than anything bigger
            return ObjectUtils.compare(getValue(), ((LessThanSpecification<T>) other).getValue()) <= 0;
        }
        return false;
    }

}