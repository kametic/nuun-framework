package org.nuunframework.universalvisitor.api;

import java.lang.reflect.Field;



/**
 *
 * @author Epo Jemba
 * @author Pierre Thirouin
 *
 */
public interface Predicate {

	boolean apply( Field input);

	Predicate TRUE = new Predicate() {
		@Override
		public boolean apply(Field input) {
			return true;
		}
	};

}
