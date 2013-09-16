/**
 * 
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
 * 
 */
package org.nuunframework.cli.samples;

import org.nuunframework.cli.NuunOption;

/**
 * @author ejemba
 */
public class Holder
{
    @NuunOption(opt="o1",longOpt="option1",description="the long description of opt number 1",arg=true)
    private String option1;
    
    @NuunOption(opt="o2",longOpt="option2",description="the long description of opt number 2",arg=true,required=true)
    private String option2;
    
    public String getOption1()
    {
        return option1;
    }
    
    public String getOption2()
    {
        return option2;
    }

    
    
    
    
    
}
