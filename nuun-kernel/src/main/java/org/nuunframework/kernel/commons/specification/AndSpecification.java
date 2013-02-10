package org.nuunframework.kernel.commons.specification;

public class AndSpecification<T> extends CompositeSpecification<T> {

    /**
     * 
     * @param lhs
     * @param rhs
     */
    public AndSpecification(Specification<? super T> lhs, Specification<? super T> rhs) {
        super(lhs, rhs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSatisfiedBy(T candidate) {
        return getLhs().isSatisfiedBy(candidate) && getRhs().isSatisfiedBy(candidate);
    }

}