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
package org.nuunframework.cli;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.nuunframework.kernel.Kernel;

/**
 * @author ejemba
 *
 */
public class NuunCliService
{
    
    Kernel kernel = null;
    
    public int startSync(String[] args ) throws Exception
    {
    	return startSync(args,null);
    }
    
    public int startSync(String[] args , Callable<Integer> callable) throws Exception
    {
        int returnCode = 0;
    	
		if (kernelStart(args)) 
		{
			if (callable != null) 
			{
				kernel.getMainInjector().injectMembers(callable);
				returnCode = callable.call();
			}
		}
    	
    	return returnCode;
    }

    public Future<Integer> startAsync(String[] args ) throws Exception
    {
    	return startAsync(args,null);
    }
    
    public Future<Integer> startAsync(String[] args , Callable<Integer> callable) throws Exception
    {
    	Future<Integer> future = null;
    	
    	if (kernelStart(args)) 
    	{
    		if (callable != null) 
    		{
    			kernel.getMainInjector().injectMembers(callable);
    			
    			ExecutorService newSingleThreadExecutorService = Executors.newSingleThreadExecutor();
    			
				future = newSingleThreadExecutorService.submit(callable); 
    		}
    	} 
    	else
    	{
			
		}
    	
    	return future;
    }

	private boolean kernelStart(String[] args) {
		if (kernel == null)		
		{
			kernel = Kernel.createKernel().withContainerContext(args).build();
			kernel.init();
			kernel.start();
			
			Runtime.getRuntime().addShutdownHook(new Thread()
		        {
		            @Override
		            public void run()
		            {
		                NuunCliService.this.stop();
		            }
		        });
			
			return true;
		}
		else {
			return false;
		}
	}
    
    public void stop()
    {
        if (kernel != null)
        {
            kernel.stop();
            kernel = null;
        }
    }
    
    public Kernel getKernel()
    {
        return kernel;
    }
}
