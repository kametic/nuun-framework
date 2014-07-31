package org.nuunframework.universalvisitor.api;

import java.lang.reflect.AccessibleObject;
/**
 * 
 * 
 * @author Epo Jemba
 * @author Pierre Thirouin
 *
 */
public  class Node {

	
	public class Metadata {
		
		private int index;
		private Object key;
		
		
		
		public Metadata() {
			index = -1;
			key = null;
		}
		public Metadata(Object key) {
			this.key = key;
		}
		
		public Metadata(int index) {
			this.index = index;
		}
		
		public int index() {
			return index;
		}
		
		public Object key() {
			return key;
		}
		
		
	}
	
	private Object instance;
	private Metadata metadata = new Metadata();
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
	
	protected Node(Object instance, AccessibleObject accessibleObject, int level , Metadata metadata) {
		this(instance, accessibleObject);
		this.level = level;
		this.metadata  = metadata;
	}
	
	protected Node(Object instance, AccessibleObject accessibleObject, int level , int index) {
		this(instance, accessibleObject);
		this.level = level;
		metadata  = new Metadata(index);
	}
	
	protected Node(Object instance, AccessibleObject accessibleObject, int level , Object key) {
		this(instance, accessibleObject);
		this.level = level;
		metadata  = new Metadata(key);
	}

	public Object instance() {
		return instance;
	}

	public int level() {
		return level;
	}
	
	public Metadata metadata() {
		return metadata;
	}
	
	public AccessibleObject accessibleObject() {
		return accessibleObject;
	}


	public void accessibleObject(AccessibleObject accessibleObject) {
		this.accessibleObject = accessibleObject;
	}
	
	
	
	@Override
	public String toString() {
		return "Node [instance=" + instance + ", accessibleObject="
				+ accessibleObject + ", level=" + level + "]";
	}

}