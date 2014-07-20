package org.nuunframework.universalvisitor;

import java.lang.reflect.AccessibleObject;



/**
 *
 * @author Epo Jemba
 * @author Pierre Thirouin
 *
 */
public interface Predicate {
	/**
	 * Returns the result of applying this predicate to {@code input}. This
	 * method is <i>generally expected</i>, but not absolutely required, to have
	 * the following properties:
	 *
	 * <ul>
	 * <li>Its execution does not cause any observable side effects.
	 * <li>The computation is <i>consistent with equals</i>; that is,
	 * {@link Objects#equal Objects.equal}{@code (a, b)} implies that
	 * {@code predicate.apply(a) == predicate.apply(b))}.
	 * </ul>
	 *
	 * @throws NullPointerException
	 *             if {@code input} is null and this predicate does not accept
	 *             null arguments
	 */
	boolean apply( AccessibleObject input);

	Predicate TRUE = new Predicate() {
		@Override
		public boolean apply(AccessibleObject input) {
			return true;
		}
	};

}
