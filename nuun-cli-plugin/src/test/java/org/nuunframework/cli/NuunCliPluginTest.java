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

import org.apache.commons.cli.Options;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nuunframework.cli.samples.Holder;
import org.nuunframework.kernel.Kernel;

/**
 * @author ejemba
 *
 */
public class NuunCliPluginTest
{

    
    private NuunCliService nuunCliService;
    
    
    @Before
    public void init () throws Exception
    {
        nuunCliService = new NuunCliService();

        String[] argsStrings = new String[] { 
                "-o1" , "cli1" , //  
                "--option2" , "cli2" , // 
                "-o3" ,  //
                "--option4" , "1/2/3" , // 
                "-o5" , "cli5" , //  
                "arg1" , "arg2" // 
        };
        
        nuunCliService.startSync(argsStrings );
        
    }
    
    @After
    public void uninit()
    {
        nuunCliService.stop();
    }
    

    @Test
    public void testInjections ()
    {
        Kernel kernel = nuunCliService.getKernel();
        Holder holder = kernel.getMainInjector().getInstance(Holder.class);
        
        assertThat( holder.getOption1() ).isNotNull();
        assertThat( holder.getOption1() ).isEqualTo("cli1");

        assertThat( holder.getOption2() ).isNotNull();
        assertThat( holder.getOption2() ).isEqualTo("cli2");

        assertThat( holder.getOption3() ).isNotNull();
        assertThat( holder.getOption3() ).isTrue();
        
        assertThat( holder.getOption4() ).isNotNull();
        assertThat( holder.getOption4() ).isEqualTo(new String[]{ "1" , "2" , "3" });

        assertThat( holder.getOption5() ).isNotNull();
        assertThat( holder.getOption5() ).isEqualTo("cli5");
        
        assertThat( holder.getArg() ).isNotNull();
        assertThat( holder.getArg() ).isEqualTo(new String[]{ "arg1" , "arg2" });
    }
    
    @Test
    public void testOptions()
    {
        Kernel kernel = nuunCliService.getKernel();
        Options options = kernel.getMainInjector().getInstance(Options.class);
        
        assertThat(options).isNotNull();
        assertThat(options.getOptions().size()).isEqualTo(5);
        
        assertThat(options.getOption("o1")).isNotNull();
        assertThat(options.getOption("o1").getDescription()).isEqualTo("the long description of opt number 1");
        assertThat(options.getOption("o1").getLongOpt()).isEqualTo("option1");
        assertThat(options.getOption("o1").isRequired()).isFalse();
        
        assertThat(options.getOption("o2")).isNotNull();
        assertThat(options.getOption("o2").getDescription()).isEqualTo("the long description of opt number 2");
        assertThat(options.getOption("o2").getLongOpt()).isEqualTo("option2");
        assertThat(options.getOption("o2").isRequired()).isTrue();
        
        assertThat(options.getRequiredOptions().size()).isEqualTo(4);
    }

}
