package org.nuunframework.universalvisitor.sample.simple;

import org.nuunframework.universalvisitor.sample.Alphabet;

/**
 *
 * 
 *
 */
@Alphabet
public class A {
	
	public B b;
	public String name;
	
	public A() {
	}
	public A(String name) {
		this.name = name;
	}

}
