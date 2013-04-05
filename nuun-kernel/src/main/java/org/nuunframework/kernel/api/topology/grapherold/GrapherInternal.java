package org.nuunframework.kernel.api.topology.grapherold;

import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nuunframework.kernel.commons.specification.Specification;

public class GrapherInternal implements Grapher, InstanceBuilder , ReferenceBuilder, ReferenceTargetBuilder , ReferenceOptionsBuilder
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
        
        public instance(String name , Class<?> type )
        {
            this.name = name;
            this.type = type;
        }
        
    }

    public static class reference
    {
        public final String name;
        public final String target;
        public final String source;
        public boolean optionnal = false;

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
    public InstanceBuilder newInstance(String name, Class<?> type)
    {
        checkNotExisting(name);
        currentInstance = new instance(name,type);
        all.put( name , currentInstance);
        instances.add(currentInstance);
        instancesByName.put(name, currentInstance);
        return this;
    }
    
    @Override
    public InstanceBuilder newInstance(String name, Specification<Class<?>> typeSubset)
    {
        checkNotExisting(name);
//        currentInstance = new instance(name,type);
        all.put( name , currentInstance);
        instances.add(currentInstance);
        instancesByName.put(name, currentInstance);
        return this;
    }
    
    @Override
    public void asWildcard()
    {
     
    }

    String currentName;
    String currentSource;
    String currentTarget;

    
    @Override
    public ReferenceBuilder newReference(String referenceName)
    {
        boolean expression = 
                (   currentName !=  null && currentSource != null &&  currentTarget != null) 
                || (currentName ==  null && currentSource == null &&  currentTarget == null);
        checkState( expression  , "Invalid Use of the dsl");
        
        currentName = currentSource = currentTarget = null;
        currentReference = null;
         
        // checkNotExisting(referenceName);
        // reference can have the same name
        currentName = referenceName;
        return this;
    }

    @Override
    public ReferenceTargetBuilder from(String sourceInstance)
    {
        checkState(currentSource == null, "currentSource should be null.");
        checkState(all.get(sourceInstance) != null, "%s should exist." , sourceInstance);
        currentSource = sourceInstance;
        return this;
    }
    
    @Override
    public ReferenceOptionsBuilder to(String targetInstance)
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

    @Override
    public void asOptionnal()
    {
        checkState(currentReference != null , "currentReference should not be null");
        currentReference.optionnal = true;
    }
    
    private void checkNotExisting(String name)
    {
        checkState(all.get(name) == null, "%s already exists.", name);
    }

    @Override
    public InstanceBuilder newInstance(String name)
    {
        return null;
    }
}
