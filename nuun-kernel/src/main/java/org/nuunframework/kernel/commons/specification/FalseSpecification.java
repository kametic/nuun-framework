package org.nuunframework.kernel.commons.specification;
public class FalseSpecification extends AbstractSpecification<Object> {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSatisfiedBy(Object candidate) {
        return false;
    }

}