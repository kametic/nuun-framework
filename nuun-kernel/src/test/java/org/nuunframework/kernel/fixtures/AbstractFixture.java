/**
 * Copyright (C) 2013 Kametic <epo.jemba@kametic.com>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * or any later version
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
