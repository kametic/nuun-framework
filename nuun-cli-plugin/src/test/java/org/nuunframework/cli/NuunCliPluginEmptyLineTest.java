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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nuunframework.kernel.plugin.PluginException;

/**
 * @author ejemba
 *
 */
public class NuunCliPluginEmptyLineTest
{

    
    private NuunCliService nuunCliService;
    
    
    @Before
    public void init () throws Exception
    {
        nuunCliService = new NuunCliService();
        
    }
    
    @BeforeClass
    public static void initStatic ()
    {
        System.setProperty("debug", "on");
    }
    
    
    @Test
    public void emptyLineShouldBeAccepted ()
    {
        String[] argsStrings = new String[] { };
        
        try
        {
            nuunCliService.startSync(argsStrings );
        }
        catch (Exception e)
        {
            assertThat(e).isInstanceOf(PluginException.class);
            
            Object expected = "no command line context were fullfilled. see previous errors.";
            assertThat(e.getMessage()).isEqualTo(expected );
            
        }
        
    }
    
    @After
    public void uninit()
    {
        nuunCliService.stop();
    }
    

}
