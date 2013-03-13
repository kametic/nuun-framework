package org.nuunframework.kernel.plugin.dummy4;

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

public class DummyPlugin4 extends AbstractPlugin
{

    private Specification<Class<?>> specification;
    public Collection<Class<?>> collection;


    public DummyPlugin4()
    {
    }

    @Override
    public String name()
    {
        return "dummuyPlugin4";
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Collection<BindingRequest> bindingRequests()
    {
        Specification<Class<?>> specification = and( classIsAnnotatedWith(MarkerSample5.class) , classIsImplementing(Interface2.class));
        
        assertThat( specification.isSatisfiedBy(Pojo1.class) ).isFalse();
        assertThat( specification.isSatisfiedBy(Pojo2.class) ).isTrue();
        
        return bindingRequestsBuilder().specification(specification , Scopes.SINGLETON).build();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests()
    {
        specification = and( classIsAnnotatedWith(MarkerSample.class) , classIsImplementing(Interface1.class));

        assertThat( specification.isSatisfiedBy(Pojo1.class) ).isTrue();
        assertThat( specification.isSatisfiedBy(Pojo2.class) ).isFalse();
        
        return classpathScanRequestBuilder().specification(specification).build();
    }
    
    
    @Override
    public void init(InitContext initContext)
    {
        Map<Specification, Collection<Class<?>>> scannedTypesBySpecification = initContext.scannedTypesBySpecification();
        
        collection = scannedTypesBySpecification.get(specification);
        
        assertThat(collection).isNotEmpty();
        assertThat(collection).hasSize(1);
        assertThat(collection).containsOnly(Pojo1.class);
    }
    
    
    @Override
    public String pluginPackageRoot()
    {
        return DummyPlugin4.class.getPackage().getName();
    }

}
