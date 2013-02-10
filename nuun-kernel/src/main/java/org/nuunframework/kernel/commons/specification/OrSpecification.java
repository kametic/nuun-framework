package org.nuunframework.kernel.commons.specification;
public class OrSpecification<T> extends CompositeSpecification<T> {

    /**
     * 
     * @param lhs
     * @param rhs
     */
    public OrSpecification(Specification<? super T> lhs, Specification<? super T> rhs) {
        super(lhs, rhs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSatisfiedBy(T candidate) {
        return getLhs().isSatisfiedBy(candidate) || getRhs().isSatisfiedBy(candidate);
    }

}