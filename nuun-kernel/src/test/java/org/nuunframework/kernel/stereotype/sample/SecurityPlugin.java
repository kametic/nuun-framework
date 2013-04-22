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

import org.aopalliance.intercept.MethodInterceptor;
import org.nuunframework.kernel.plugin.AbstractPlugin;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class SecurityPlugin extends AbstractPlugin
{

    
    private List<String> list;

    public SecurityPlugin(List<String> list)
    {
        this.list = list;
    }
    
    @Override
    public String name()
    {
        return "security";
    }
    
    @SecurityConcern
    public static class Module extends AbstractModule
    {
    	private List<String> list;
		private String name;
    	
		public Module(String name , List<String> list) {
			this.name = name;
			this.list = list;
		}
        @Override
        protected void configure()
        {
            MethodInterceptor interceptor = new ConcernInterceptor( list , name);
            bindInterceptor(Matchers.any(), Matchers.any(), interceptor );
        }
    }
    
    @Override
    public Object dependencyInjectionDef()
    {
        return new Module(name() , list);
    }

}
