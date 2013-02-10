package org.nuunframework.kernel.commons.specification;
public class TrueSpecification extends AbstractSpecification<Object> {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSatisfiedBy(Object candidate) {
        return true;
    }

}