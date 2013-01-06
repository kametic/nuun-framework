package org.nuunframework.kernel.commons.dsl;

public interface DSLNode {
	DSLNode ROOT = new DSLNode() {
		@Override
		public DSLNode parent() {return null;}

		@Override
		public DSLToken token() {return null;}
	};
	DSLNode parent();
	DSLToken token();
}