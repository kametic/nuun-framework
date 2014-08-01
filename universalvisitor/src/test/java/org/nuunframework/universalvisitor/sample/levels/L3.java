/*
 *  
 */
package org.nuunframework.universalvisitor.sample.levels;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * 
 * @author epo.jemba@kametic.com
 *
 */
public class L3 {
	
	private String name3;
	
	public L3(String name) {
		name3 = name;
	}
	
	
	@SuppressWarnings("serial")
	Map<K1, L4> mapL4 = new HashMap<K1, L4>() {{
		put(new K1(1) , new L4("l4-1"));
		put(new K1(2)  , new L4("l4-2"));
		put(new K1(3)  , new L4("l4-3"));
		put(new K1(4)  , new L4("l4-4"));
		
	}};

}
