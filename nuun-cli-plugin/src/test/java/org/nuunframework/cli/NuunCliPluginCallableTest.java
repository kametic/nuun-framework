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

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nuunframework.cli.samples.DummyRunnable;

/**
 * @author ejemba
 *
 */
public class NuunCliPluginCallableTest
{

    
    private NuunCliService nuunCliService;
    
    
    @Before
    public void init ()
    {
        nuunCliService = new NuunCliService();
    }
    
    @After
    public void uninit()
    {
        nuunCliService.stop();
    }
    

    @Test
    public void startSync_works_as_expected () throws Exception
    {
    	List<String> list = Arrays.asList("-o1" , "cli1" , "--option2" , "cli2" , "-o3" , "-o4" , "zob");
    	String[] args = (String[]) list.toArray(new String[list.size()]);
    	
    	DummyRunnable dummyRunnable = new DummyRunnable();
    	Integer code = nuunCliService.startSync( args, dummyRunnable );
    	
    	assertThat(code).isEqualTo(0);
    	
    	assertThat(dummyRunnable.done()).isTrue();
    }
    @Test
    public void startAsync_works_as_expected () throws Exception
    {
    	List<String> list = Arrays.asList("-o1" , "cli1" , "--option2" , "cli2" , "-o3" , "-o4" , "ezre");
    	String[] args = (String[]) list.toArray(new String[list.size()]);
    	
    	DummyRunnable dummyRunnable = new DummyRunnable();
    	Future<Integer> future = nuunCliService.startAsync( args, dummyRunnable );
    	
    	Integer code = future.get();
    	
    	assertThat(code).isEqualTo(0);
    	
    	assertThat(dummyRunnable.done()).isTrue();
    }
    

}
