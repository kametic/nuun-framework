package org.nuunframework.universalvisitor.core;

import java.lang.reflect.AnnotatedElement;

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
	private AnnotatedElement annotatedElement;
	protected int level = 0;


	protected NodeDefault(Object instance, AnnotatedElement annotatedElement ) {
		super();
		this.instance = instance;
		this.annotatedElement = annotatedElement;
	}
	
	protected NodeDefault(Object instance, AnnotatedElement annotatedElement, int level ) {
		this(instance, annotatedElement);
		this.level = level;
	}
	
	protected NodeDefault(Object instance, AnnotatedElement annotatedElement, int level , Metadata metadata) {
		this(instance, annotatedElement);
		this.level = level;
		this.metadata  = metadata;
	}
	
	protected NodeDefault(Object instance, AnnotatedElement annotatedElement, int level , int index) {
		this(instance, annotatedElement);
		this.level = level;
		metadata  = new Metadata(index);
	}
	
	protected NodeDefault(Object instance, AnnotatedElement annotatedElement, int level , Object key) {
		this(instance, annotatedElement);
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
    public AnnotatedElement annotatedElement() {
    	return annotatedElement;
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
	


	public void annotatedElement(AnnotatedElement annotatedElement) {
		this.annotatedElement = annotatedElement;
	}
	
	
	
	@Override
	public String toString() {
		return "Node [instance=" + instance + ", annotatedElement="
				+ annotatedElement + ", level=" + level + "]";
	}

}