package org.nuunframework.kernel.plugin.dummy5;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Collection;
import java.util.Map;

import org.nuunframework.kernel.commons.specification.Specification;
import org.nuunframework.kernel.context.InitContext;
import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.nuunframework.kernel.plugin.request.BindingRequest;
import org.nuunframework.kernel.plugin.request.ClasspathScanRequest;
import org.nuunframework.kernel.scanner.sample.MarkerSample;

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
                .descendentTypeOf(GrandParentClass.class) // 
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
    public void init(InitContext initContext)
    {
        Map<Class<?>, Collection<Class<?>>> scannedSubTypesByAncestorClass = initContext.scannedSubTypesByAncestorClass();
        
        collection = scannedSubTypesByAncestorClass.get(GrandParentClass.class);
        
        assertThat(collection).isNotEmpty();
        assertThat(collection).hasSize(2);
        assertThat(collection).containsOnly(DescendantFromClass.class , ParentClass.class);
    }
    
    
    @Override
    public String pluginPackageRoot()
    {
        return DummyPlugin5.class.getPackage().getName();
    }

}
