package org.nuunframework.kernel.commons.specification;
public class OrSpecification<T> extends CompositeSpecification<T> {

    /**
     * 
     * @param lhs
     * @param rhs
     */
    public OrSpecification(Specification<? super T>... specificationParticipants) {
        super(specificationParticipants);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSatisfiedBy(T candidate) {
        boolean result = false;
        
        for(Specification<? super T> participant : this.childSpecifications)
        {
            result |= participant.isSatisfiedBy(candidate);
        }
        
        return result;
    }

}