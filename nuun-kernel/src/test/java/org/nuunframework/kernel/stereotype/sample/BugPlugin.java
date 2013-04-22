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
package org.nuunframework.kernel.stereotype.sample;

import java.util.List;

import org.nuunframework.kernel.plugin.AbstractPlugin;

import com.google.inject.AbstractModule;

public class BugPlugin extends AbstractPlugin
{

    
    private List<String> list;

    public BugPlugin(List<String> list)
    {
        this.list = list;
    }
    
    @Override
    public String name()
    {
        return "bug";
    }
    
    @BugConcern
    public static class Module extends AbstractModule
    {
		public Module(String name , List<String> list) {
		}
        @Override
        protected void configure()
        {
        }
    }
    
    @Override
    public Object dependencyInjectionDef()
    {
        return new Module ( name() , list );
    }

}
