package org.nuunframework.kernel.commons.specification;

import org.apache.commons.lang.ObjectUtils;

public abstract class CompareToSpecification<T extends Comparable<T>> extends AbstractSpecification<T> {
    private final T value;

    /**
     * Construct a new {@link CompareToSpecification}.
     * @param value the value that our candidate should compared to
     */
    public CompareToSpecification(T value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSatisfiedBy(T candidate) {
        int comparison = ObjectUtils.compare(candidate, value);
        return this.isSatisfyingComparison(comparison);
    }

    /**
     * See if a comparison satisfies all the requirements expressed in this specification.
     * @param comparison the comparison value between our candidate property and the specified value
     * @return {@code true} if the comparison has been accepted, otherwise {@code false}
     */
    protected abstract boolean isSatisfyingComparison(int comparison);

    /**
     * Retrieve the value we are comparing our candidates to.
     * @return the current value we are comparing to
     */
    protected T getValue() {
        return value;
    }

}