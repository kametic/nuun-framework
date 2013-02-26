package org.nuunframework.kernel.commons.specification;

public class AndSpecification<T> extends CompositeSpecification<T> {

    /**
     * 
     * @param lhs
     * @param rhs
     */
    @SuppressWarnings("unchecked")
    public AndSpecification(Specification<? super T>... specificationParticipants) {
        super(specificationParticipants);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSatisfiedBy(T candidate) {
        boolean result = true;
        
        for(Specification<? super T> participant : this.childSpecifications)
        {
            result &= participant.isSatisfiedBy(candidate);
        }
        
        return result;
    }

}