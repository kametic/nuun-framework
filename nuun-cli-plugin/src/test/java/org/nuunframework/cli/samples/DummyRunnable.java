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
package org.nuunframework.cli.samples;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.nuunframework.kernel.plugins.logs.NuunLog;
import org.slf4j.Logger;

/**
 *
 * 
 * @author epo.jemba@kametic.com
 *
 */
public class DummyRunnable implements Callable<Integer> {

	
	boolean done = false;
	
	@NuunLog
	Logger logger;
	
	@Inject
	Holder holder;
	
	@Override
	public Integer call() {
		logger.debug("Start runner");
		
		logger.info(  holder.getOption1() + " " + holder.getOption2());
		done = true;
		logger.debug("End runner");
		
		return 0;
	}
	
	public boolean done()
	{
		return done;
	}

}
