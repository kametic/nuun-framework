package org.nuunframework.universalvisitor.core;

import java.lang.reflect.AccessibleObject;

import org.nuunframework.universalvisitor.api.Metadata;
import org.nuunframework.universalvisitor.api.Node;
/**
 * 
 * 
 * @author Epo Jemba
 * @author Pierre Thirouin
 *
 */
public  class NodeDefault implements Node{

	private Object instance;
	private Metadata metadata =null;
	private AccessibleObject accessibleObject;
	protected int level = 0;


	protected NodeDefault(Object instance, AccessibleObject accessibleObject ) {
		super();
		this.instance = instance;
		this.accessibleObject = accessibleObject;

	}
	
	protected NodeDefault(Object instance, AccessibleObject accessibleObject, int level ) {
		this(instance, accessibleObject);
		this.level = level;
	}
	
	protected NodeDefault(Object instance, AccessibleObject accessibleObject, int level , Metadata metadata) {
		this(instance, accessibleObject);
		this.level = level;
		this.metadata  = metadata;
	}
	
	protected NodeDefault(Object instance, AccessibleObject accessibleObject, int level , int index) {
		this(instance, accessibleObject);
		this.level = level;
		metadata  = new Metadata(index);
	}
	
	protected NodeDefault(Object instance, AccessibleObject accessibleObject, int level , Object key) {
		this(instance, accessibleObject);
		this.level = level;
		metadata  = new Metadata(key);
	}
	
    @Override
	public Object instance() {
		return instance;
	}

    @Override
	public int level() {
		return level;
	}
	
    @Override
	public Metadata metadata() {
		return metadata;
	}
	
    @Override
    public AccessibleObject accessibleObject() {
    	return accessibleObject;
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
	


	public void accessibleObject(AccessibleObject accessibleObject) {
		this.accessibleObject = accessibleObject;
	}
	
	
	
	@Override
	public String toString() {
		return "Node [instance=" + instance + ", accessibleObject="
				+ accessibleObject + ", level=" + level + "]";
	}

}