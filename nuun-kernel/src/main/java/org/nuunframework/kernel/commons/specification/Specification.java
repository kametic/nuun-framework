package org.nuunframework.kernel.commons.specification;


public interface Specification<T> {

    /**
     * See if an object satisfies all the requirements expressed in this specification.
     * 
     * @param candidate the object being verified
     * @return {@code true} if the requirements are satisfied, otherwise {@code false}
     */
    boolean isSatisfiedBy(T candidate);


    /**
     * Construct a new specification that verifies if both this, as the provided,
     * specification has been satisfied.
     * 
     * @param other the specification we want to include
     * @return new composite specification, evaluates using a logical AND
     */
    Specification<T> and(Specification<? super T> other);

    /**
     * Construct a new specification that verifies if either this, or the provided,
     * specification has been satisfied.
     * 
     * @param other the specification we want to include
     * @return new composite specification, evaluates using a logical OR
     */
    Specification<T> or(Specification<? super T> other);

    /**
     * Construct a new specification that verifies if this specification remains unsatisfied.
     * 
     * @return new specification, negating the current specification
     */
    Specification<T> not();

}