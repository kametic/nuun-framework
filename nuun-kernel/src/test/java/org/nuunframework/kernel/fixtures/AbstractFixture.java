package org.nuunframework.kernel.fixtures;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public abstract class AbstractFixture<UT> implements TestRule {

	private UT underTest;
	
	@Override
	public Statement apply(final Statement base, Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				before();
				try {
					base.evaluate();
				} finally {
					after();
				}
			}
		};		
	}
	
	/**
	 * before() 
	 * @return the unit under test.
	 */
	public UT getUnderTest ()
	{
		return this.underTest;
	}
	
	/**
	 * Override to set up your specific external resource.
	 * @throws if setup fails (which will disable {@code after}
	 */
	protected void before() throws Throwable
	{
		this.underTest = createUnitUnderTest ();
	}

	abstract protected UT createUnitUnderTest();

	/**
	 * Override to tear down your specific external resource.
	 */
	protected void after()
	{
		this.underTest = null;
	}

}
