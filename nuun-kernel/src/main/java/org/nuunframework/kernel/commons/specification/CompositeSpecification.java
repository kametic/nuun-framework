package org.nuunframework.kernel.commons.specification;

public abstract class CompositeSpecification<T> extends AbstractSpecification<T> {
    private final Specification<? super T> lhs;
    private final Specification<? super T> rhs;

    /**
     * Construct a new {@link CompositeSpecification}.
     * @param lhs left hand side specification
     * @param rhs right hand side specification
     */
    public CompositeSpecification(Specification<? super T> lhs, Specification<? super T> rhs) {
        super();
        if (lhs == null || rhs == null) {
            throw new IllegalArgumentException("Composed specifications cannot be null.");
        }
        this.lhs = lhs;
        this.rhs = rhs;
    }

    /**
     * Retrieve the left hand side sub-specification.
     * @return left hand side specification
     */
    protected Specification<? super T> getLhs() {
        return lhs;
    }

    /**
     * Retrieve the right hand side sub-specification.
     * @return right hand side specification
     */
    protected Specification<? super T> getRhs() {
        return rhs;
    }

}