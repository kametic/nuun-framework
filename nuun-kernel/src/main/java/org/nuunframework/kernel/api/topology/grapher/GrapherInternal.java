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
package org.nuunframework.kernel.api.topology.grapher;


import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nuunframework.kernel.commons.specification.Specification;

public class GrapherInternal implements Grapher, Grapher2, Grapher3, Grapher5, Grapher6
{

    final Map<String, Object> all;
    public final Map<String, instance> instancesByName;
    public final List<instance>      instances;
    public final List<reference>     references;
    
    private reference currentReference;
    private instance currentInstance;

    public static class instance
    {
        public final String         name;
        public final Class<?>       type;
        public final Set<reference> references = new HashSet<GrapherInternal.reference>();
        public final Collection<String> subInstances = new HashSet<String>();
        public final Map<Object, Object> properties = new HashMap<Object, Object>();
        public final Specification<Class<?>> specification;
        
        public instance(String name )
        {
            this.name = name;
            this.type = null;
            this.specification = null;
        }
        public instance(String name , Class<?> type )
        {
            this.name = name;
            this.type = type;
            this.specification = null;
        }
        public instance(String name, Specification<Class<?>> specification)
        {
            this.name = name;
            this.specification = specification;
            this.type = null;
        }
        
    }

    public static class reference
    {
        public final String name;
        public final String target;
        public final String source;
        public final Map<Object, Object> properties = new HashMap<Object, Object>();

        public reference(String name, String target , String source)
        {
            this.name = name;
            this.target = target;
            this.source = source;
        }
    }
    
    public GrapherInternal()
    {
        all = new HashMap<String, Object>();
        instancesByName = new HashMap<String, instance>();
        instances = new ArrayList<GrapherInternal.instance>();
        references = new ArrayList<GrapherInternal.reference>();
    }

    
    @Override
    public Grapher3 newInstance(String name)
    {
        clearReference();
        checkNotExisting(name);
        currentInstance = new instance(name);
        all.put( name , currentInstance);
        instances.add(currentInstance);
        instancesByName.put(name, currentInstance);
        return this;
    }

    @Override
    public Grapher3 newInstance(String name, Class<?> type)
    {
        clearReference();
        checkNotExisting(name);
        currentInstance = new instance(name,type);
        all.put( name , currentInstance);
        instances.add(currentInstance);
        instancesByName.put(name, currentInstance);
        return this;
    }

    @Override
    public Grapher3 newSpecificationInstance(String name,Specification<Class<?>> specification)
    {
        clearReference();
        checkNotExisting(name);
        currentInstance = new instance(name,specification);
        all.put( name , currentInstance);
        instances.add(currentInstance);
        instancesByName.put(name, currentInstance);
        return this;
    }
    
    @Override
    public Grapher6 newCompositeInstance(String name)
    {
        clearReference();
        checkNotExisting(name);
        currentInstance = new instance(name);
        all.put( name , currentInstance);
        instances.add(currentInstance);
        instancesByName.put(name, currentInstance);
        return this;
    }
    
    @Override
    public Grapher3 withChildInstances ( String... names ) 
    {
        checkState(currentInstance != null , "currentInstance should not be null");
        checkState(names != null , "Child Instance should be not null");
        
        for (String name : names)
        {
            currentInstance.subInstances.add( name );
        }
        
        return this;
    }
    
    ////////////////////////////////////////////////////////
    
    String currentName;
    String currentSource;
    String currentTarget;
    
    @Override
    public Grapher2 newReference(String referenceName)
    {
        clearInstance();
        boolean expression = 
                (   currentName !=  null && currentSource != null &&  currentTarget != null) 
                || (currentName ==  null && currentSource == null &&  currentTarget == null);
        checkState( expression  , "Invalid Use of the dsl");
        
        currentName = currentSource = currentTarget = null;
        currentReference = null;
         
        currentName = referenceName;
        
        
        return this;
    }

    @Override
    public Grapher5 from(String sourceInstance)
    {
        checkState(currentSource == null, "currentSource should be null.");
        checkState(all.get(sourceInstance) != null, "%s should exist." , sourceInstance);
        currentSource = sourceInstance;
        return this;
    }

    @Override
    public Grapher3 to(String targetInstance)
    {
        checkState(currentTarget == null, "currentTarget should be null.");
        checkState(all.get(targetInstance) != null, "%s should exist." , targetInstance);
        
        currentTarget = targetInstance;
        currentReference = new reference(currentName, targetInstance , currentSource);
        references.add(currentReference);
        instancesByName.get(currentSource).references.add(currentReference);
        all.put( currentName , currentReference);
        // we reinit the whole stuff.
        return this;
    }
    
    ////////////////////////////////////////////////////////

    @Override
    public Grapher3 withProperty(Object key, Object value)
    {
        if (currentReference != null)
        {
            currentReference.properties.put(key, value);
        }
        if (currentInstance != null )
        {
            currentInstance.properties.put(key, value);
        }
        
        return this;
    }
    
    ////////////////////////////////////////////////////////

    private void clearInstance()
    {
        currentReference = null;
    }
    private void clearReference()
    {
        currentName = currentSource = currentTarget = null;
    }
    
    private void checkNotExisting(String name)
    {
        checkState(all.get(name) == null, "%s already exists.", name);
    }

}
