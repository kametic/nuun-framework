package org.nuunframework.universalvisitor.api;

import java.lang.reflect.Field;



/**
 *
 * @author Epo Jemba
 * @author Pierre Thirouin
 *
 */
public interface Filter {

	boolean retains( Field input);

	Filter TRUE = new Filter() {
		@Override
		public boolean retains(Field input) {
			return true;
		}
	};

}
