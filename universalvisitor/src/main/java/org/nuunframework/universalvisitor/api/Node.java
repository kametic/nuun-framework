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

	
	public static class Metadata {
		
		private int index=-1;
		private Object key=null;
		
		public Metadata() {
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
		
		@Override
		public String toString() {
			String metadata = "";
			
			if (index > -1) {
				metadata = "["+index+"]";
			}
			
			if (key != null) {
				metadata += "["+key+"]";
			}
			
			
			return metadata;
		}
		
		
		
		
	}
	
	private Object instance;
	private Metadata metadata =null;
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
	
	public Metadata metadata (int index) {
		if (metadata == null) {
			metadata = new Metadata(index);
		}
		
		return metadata;
	}
	
	public Metadata metadata (Object key) {
		if (metadata == null) {
			metadata = new Metadata(key);
		}
		return metadata;
	}
	
	public Node metadata (Metadata metadata) {
		this.metadata = metadata;
		return this;
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