package org.nuunframework.kernel.commons.specification;
/**
 * Specification that can compare its logic to other specifications.
 * 
 * @author Jeroen van Schagen
 * @since 22-02-2011
 *
 * @param <T> type of candidates being checked on
 */
public interface SubsumableSpecification<T> extends Specification<T> {

    /**
     * Check whether this specification <i>subsumes</i> another specification, which
     * means that every satisfying candidate of this specification also satisfies the
     * other specification. It could be described using conventional notation:
     * <p>
     * {@code this specification -> other specification}
     * <p>
     * So, whenever this specification is {@code true}, the other specification is
     * also {@code true}.
     * 
     * @param other the other specification that should be checked
     * @return whether this specification subsumes the other specification
     */
    boolean subsumes(Specification<T> other);
    
}