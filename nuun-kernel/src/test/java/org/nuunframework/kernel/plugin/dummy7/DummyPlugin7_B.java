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
package org.nuunframework.kernel.plugin.dummy7;

import org.nuunframework.kernel.plugin.AbstractPlugin;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
/**
 * 
 * A 2 rounds plugin
 * 
 * @author ejemba
 *
 */
public class DummyPlugin7_B extends AbstractPlugin
{

    public DummyPlugin7_B()
    {
    }
    
    @Override
    public String name()
    {
        return "dummy-plugin-7-B";
    }

    
    @Override
    public String pluginPackageRoot()
    {
        return DummyPlugin7_B.class.getPackage().getName();
    }
    
    
    @Override
    public Object dependencyInjectionOverridingDef()
    {
        return new AbstractModule()
        {
            
            @Override
            protected void configure()
            {
                bind(String.class).annotatedWith(Names.named("dep7a")).toInstance("dep7aOVER");
            }
        };
    }
    
}
