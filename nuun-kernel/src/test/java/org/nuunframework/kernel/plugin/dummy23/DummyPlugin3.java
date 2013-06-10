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
/**
 * 
 */
package org.nuunframework.kernel.plugin.dummy23;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Collection;
import java.util.Map;

//import org.nuunframework.configuration.common.NuunConfigurationPlugin;
import org.nuunframework.kernel.context.InitContext;
import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.nuunframework.kernel.plugin.InitState;
import org.nuunframework.kernel.plugin.Plugin;
import org.nuunframework.kernel.plugin.request.ClasspathScanRequest;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.name.Names;

/**
 * @author Epo Jemba
 */
public class DummyPlugin3 extends AbstractPlugin
{

    private String resourcesRegex1 = ".*.json";
    private String resourcesRegex2 = ".*-applicationContext-.*.xml";

    /*
     * (non-Javadoc)
     * @see org.nuunframework.kernel.plugin.AbstractPlugin#name()
     */
    @Override
    public String name()
    {
        return "dummyPlugin3";
    }

    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests()
    {

        return classpathScanRequestBuilder().resourcesRegex(resourcesRegex1).resourcesRegex(resourcesRegex2).build();
    }

    @Override
    public InitState init(InitContext initContext)
    {
        Map<String, Collection<String>> mapResourcesByRegex = initContext.mapResourcesByRegex();
        
        assertThat( mapResourcesByRegex.get(resourcesRegex1) ).isNotNull();
        assertThat( mapResourcesByRegex.get(resourcesRegex1) ).hasSize(1);
        assertThat( mapResourcesByRegex.get(resourcesRegex1) ).contains("org/nuunframework/kernel/resource-to-reach.json");
        
        assertThat( mapResourcesByRegex.get(resourcesRegex2) ).isNotNull();
        assertThat( mapResourcesByRegex.get(resourcesRegex2) ).hasSize(2); // TODO FIX THE CASE OF THE properties
        assertThat( mapResourcesByRegex.get(resourcesRegex2) ).contains("internal/sample1-applicationContext-business.xml");
        assertThat( mapResourcesByRegex.get(resourcesRegex2) ).contains("internal/sample2-applicationContext-persistence.xml");
        
//        NuunConfigurationPlugin confPlugin = (NuunConfigurationPlugin) initContext.pluginsRequired().iterator().next();
//        
//        assertThat(confPlugin.getConfiguration().getString("value1")).isEqualTo("lorem ipsum");
        
        return InitState.INITIALIZED;
    }
    
    @Override
    public Object dependencyInjectionDef()
    {
        return new AbstractModule()
        {
            
            @Override
            protected void configure()
            {
                bind(Key.get(String.class, Names.named("custom.name"))).toInstance("John");
                bind(Key.get(String.class, Names.named("custom.name2"))).toInstance("Jane");
//                bind(Key.get(String.class, Names.named("value1"))).toInstance();
                bindConstant().annotatedWith(Names.named("value1")).to("lorem ipsum");
            }
        };
    }

//     /* (non-Javadoc)
//     * @see org.nuunframework.kernel.plugin.AbstractPlugin#pluginsRequired()
//     */
//     @SuppressWarnings({
//            "unchecked", "rawtypes"
//    })
//    @Override
//     public Collection<Class<? extends Plugin>> requiredPlugins()
//     {
//         return (Collection) collectionOf(NuunConfigurationPlugin.class);
//     }     
     
     

}
