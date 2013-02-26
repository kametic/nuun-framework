package org.nuunframework.kernel.commons.specification;

public abstract class CompositeSpecification<T> extends AbstractSpecification<T> {
    
    protected final Specification<? super T>[] childSpecifications;

    /**
     * Construct a new {@link CompositeSpecification}.
     * @param lhs left hand side specification
     * @param rhs right hand side specification
     */
    public CompositeSpecification(Specification<? super T>... childSpecifications) {
        super();
        if (childSpecifications == null ) {
            throw new IllegalArgumentException("Composed specifications cannot be null.");
        }
        this.childSpecifications = childSpecifications;
    }

    

}