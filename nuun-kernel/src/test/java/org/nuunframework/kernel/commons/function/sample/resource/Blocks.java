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
package org.nuunframework.kernel.commons.function.sample.resource;

import org.nuunframework.kernel.commons.function.Block;
import org.nuunframework.kernel.commons.function.BlockContext;
import org.nuunframework.kernel.commons.function.LoopInfo;

public enum Blocks implements Block<Resource> 
{
    print
    {
        public void evaluate(BlockContext context,Resource resource)
        {
            System.out.println("Name = " + resource.name);
            System.out.println("Salary = " + resource.salary);
        }

    }
    ,
    print_uppercase
    {
        public void evaluate(BlockContext context,Resource resource)
        {
            System.out.println("Name = " + resource.name.toUpperCase());
            System.out.println("Salary = " + resource.salary);
        }
    }
    ,
    count_elems
    {
        public void evaluate(BlockContext context ,Resource resource)
        {
            
            if (LoopInfo.isFirst(context.loopInfo()))
            {
                context.map().put(this, 1);
            }
            else
            {
                int count = (Integer) context.map().get(this);
                count++;
                context.map().put(this, count);
                
            }
            System.out.println(  context.map().get("count") + " > " + this  + " "  + resource + " "+ LoopInfo.toString( context.loopInfo()));
        }
    }
    
    
    
    
}