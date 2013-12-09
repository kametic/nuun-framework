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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author ejemba
 *
 */
public class NuunRunnerTest
{


    
    protected String[] provideCommandLine()
    {
        return new String[] { "-a" , "-b" , "babar" , "zob" , "-Pkey1=value1", "-Pkey2=value2" };
    }
    
    @BeforeClass
    public static void initStatic ()
    {
        System.setProperty("debug", "on");
    }
    
    @Before
    public void init()
    {
        NuunRunnerDaemon.main(provideCommandLine());
    }
    
    @Test
    public void bindings() 
    {
//      assertThat(sampleCommandLineHandler).isNotNull();
    }
    
    
}
