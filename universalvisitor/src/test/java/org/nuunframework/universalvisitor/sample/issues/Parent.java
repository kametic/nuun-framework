/*
 *  
 */
package org.nuunframework.universalvisitor.sample.issues;

/**
 *
 * 
 * @author epo.jemba@kametic.com
 *
 */
public abstract class Parent implements Interface{
	
	private Issue1 parentPrivate = new Issue1();
	protected Issue1 parentProtected = new Issue1();
	        Issue1 parentPackage = new Issue1();
	public Issue1 parentPublic = new Issue1();
	
	//
	
	private void parentPrivate() {}
	protected void parentProtected() {}
	        void parentPackage() {}
	public void parentPublic() {}


}
