package org.nuunframework.universalvisitor;

import java.lang.reflect.AccessibleObject;
/**
 * 
 * 
 * @author Epo Jemba
 * @author Pierre Thirouin
 *
 */
public  class Node {

	private Object instance;
	private AccessibleObject accessibleObject;
	protected int level = 0;


	protected Node(Object instance, AccessibleObject accessibleObject ) {
		super();
		this.instance = instance;
		this.accessibleObject = accessibleObject;

	}
	
	protected Node(Object instance, AccessibleObject accessibleObject, int level ) {
		this(instance, accessibleObject);
		this.level = level;
	}

	public Object instance() {
		return instance;
	}

	public int level() {
		return level;
	}
	
	public AccessibleObject accessibleObject() {
		return accessibleObject;
	}


	public void accessibleObject(AccessibleObject accessibleObject) {
		this.accessibleObject = accessibleObject;
	}
	
	

}