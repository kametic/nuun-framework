/*
 *  
 */
package org.nuunframework.universalvisitor.sample.levels;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 
 * @author epo.jemba@kametic.com
 *
 */
public class L2 {
	
	private String name2;
	
	public L2(String name) {
		name2 = name;
	}
	
	
	@SuppressWarnings("serial")
	List<L3> l3List = new ArrayList<L3>() {{
		add(new L3("l3-1"));
		add(new L3("l3-2"));
		add(new L3("l3-3"));
	}};
	

}
