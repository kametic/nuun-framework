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
public class Issue2 extends Parent{
	
	public Issue1 issue2Public = new Issue1();
	protected Issue1 issue2Protected = new Issue1();
	         Issue1 issue2Package = new Issue1();
	private Issue1 issue2Private = new Issue1();
	
	private void issue2Public() {}
	protected void issue2Protected() {}
			void issue2Package() {}
	public void issue2Private() {}
	@Override
	public void interface1M() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int interface2M() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public long interface3M() {
		// TODO Auto-generated method stub
		return 0;
	}

}
