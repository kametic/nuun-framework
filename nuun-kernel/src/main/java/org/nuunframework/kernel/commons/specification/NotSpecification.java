package org.nuunframework.kernel.commons.specification;
public class NotSpecification<T> extends AbstractSpecification<T> {
    private final Specification<T> delegate;

    /**
     * Construct a new {@link NotSpecification}.
     * @param delegate the specification to negate
     */
    public NotSpecification(Specification<T> delegate) {
        super();
        if (delegate == null) {
            throw new IllegalArgumentException("Delegate specification cannot be null.");
        }
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSatisfiedBy(T candidate) {
        return !delegate.isSatisfiedBy(candidate);
    }

}