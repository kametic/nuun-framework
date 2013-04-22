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
package org.nuunframework.kernel.plugin.dummy5;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Collection;
import java.util.Map;

import org.nuunframework.kernel.commons.specification.Specification;
import org.nuunframework.kernel.context.InitContext;
import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.nuunframework.kernel.plugin.InitState;
import org.nuunframework.kernel.plugin.request.BindingRequest;
import org.nuunframework.kernel.plugin.request.ClasspathScanRequest;

import com.google.inject.Scopes;

public class DummyPlugin5 extends AbstractPlugin
{

    private Specification<Class<?>> specification;
    public Collection<Class<?>> collection;


    public DummyPlugin5()
    {
    }

    @Override
    public String name()
    {
        return "dummuyPlugin5";
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Collection<BindingRequest> bindingRequests()
    {
        
        return bindingRequestsBuilder() //
                .descendentTypeOf(GrandParentClass.class).withScope(Scopes.SINGLETON) //
                .metaAnnotationType(MetaMarkerSample.class).withScope(Scopes.SINGLETON) //
                .metaAnnotationRegex(".*YMetaMarker.*").withScope(Scopes.SINGLETON)
//                .descendentTypeOf(GrandParentInterface.class) // 
                .build();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests()
    {
        
        return classpathScanRequestBuilder()
                .descendentTypeOf(GrandParentClass.class) // 
                .descendentTypeOf(GrandParentInterface.class) // 
                .build();
    }
    
    
    @Override
    public InitState init(InitContext initContext)
    {
        Map<Class<?>, Collection<Class<?>>> scannedSubTypesByAncestorClass = initContext.scannedSubTypesByAncestorClass();
        
        collection = scannedSubTypesByAncestorClass.get(GrandParentClass.class);
        
        assertThat(collection).isNotEmpty();
        assertThat(collection).hasSize(2);
        assertThat(collection).containsOnly(DescendantFromClass.class , ParentClass.class);
        return InitState.INITIALIZED;
    }
    
    
    @Override
    public String pluginPackageRoot()
    {
        return DummyPlugin5.class.getPackage().getName();
    }

}
