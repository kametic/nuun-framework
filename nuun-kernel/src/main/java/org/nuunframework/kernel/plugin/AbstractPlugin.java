/**
 * 
 */
package org.nuunframework.kernel.plugin;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.nuunframework.kernel.commons.specification.AbstractSpecification;
import org.nuunframework.kernel.commons.specification.AndSpecification;
import org.nuunframework.kernel.commons.specification.NotSpecification;
import org.nuunframework.kernel.commons.specification.OrSpecification;
import org.nuunframework.kernel.commons.specification.Specification;
import org.nuunframework.kernel.commons.specification.reflect.ClassMethodsAnnotatedWith;
import org.nuunframework.kernel.commons.specification.reflect.DescendantOfSpecification;
import org.nuunframework.kernel.context.Context;
import org.nuunframework.kernel.context.InitContext;
import org.nuunframework.kernel.plugin.provider.DependencyInjectionProvider;
import org.nuunframework.kernel.plugin.request.BindingRequest;
import org.nuunframework.kernel.plugin.request.BindingRequestBuilder;
import org.nuunframework.kernel.plugin.request.ClasspathScanRequest;
import org.nuunframework.kernel.plugin.request.ClasspathScanRequestBuilder;
import org.nuunframework.kernel.plugin.request.KernelParamsRequest;
import org.nuunframework.kernel.plugin.request.KernelParamsRequestBuilder;
import org.nuunframework.kernel.plugin.request.builders.BindingRequestBuilderMain;

/**
 * @author Epo Jemba
 */
public abstract class AbstractPlugin implements Plugin
{

    protected Context                         context = null;
    protected Object                          containerContext = null;
    @SuppressWarnings("unused")
    private Map<String, String>               kernelParams;
    @SuppressWarnings("unused")
    private InitContext                       initContext;
    private final KernelParamsRequestBuilder  paramsBuilder;
    private final ClasspathScanRequestBuilder scanBuilder;
    private final BindingRequestBuilder       bindingBuilder;

    /**
     * 
     */
    public AbstractPlugin()
    {
        paramsBuilder = new KernelParamsRequestBuilder();
        scanBuilder = new ClasspathScanRequestBuilder();
        bindingBuilder = new BindingRequestBuilder();
    }

    /**
     * ============================= PLUGIN LIFE CYCLE USED BY KERNEL =============================
     **/

    @Override
    public void init(InitContext initContext)
    {
        this.initContext = initContext;
    }

    @Override
    public void stop()
    {
    }

    @Override
    public void start(Context context)
    {
        this.context = context;
    }

    @Override
    public void destroy()
    {
    }

    // /**
    // * ============================= PLUGIN Utilities Helpers =============================
    // *
    // *
    // **/

    protected KernelParamsRequestBuilder kernelParamsRequestBuilder()
    {
        paramsBuilder.reset();
        return paramsBuilder;
    }

    protected ClasspathScanRequestBuilder classpathScanRequestBuilder()
    {
        scanBuilder.reset();
        return scanBuilder;
    }

    protected BindingRequestBuilderMain bindingRequestsBuilder()
    {
        bindingBuilder.reset();
        return bindingBuilder;
    }

    protected Specification<Class<?>> or(Specification<Class<?>>... participants)
    {
        return new OrSpecification<Class<?>>(participants);
    }

    protected Specification<Class<?>> and(Specification<Class<?>>... participants)
    {
        return new AndSpecification<Class<?>>(participants);
    }

    protected Specification<Class<?>> not(Specification<Class<?>> participant)
    {
        return new NotSpecification<Class<?>>(participant);
    }

    protected Specification<Class<?>> descendantOf(Class<?> ancestor)
    {
        return new DescendantOfSpecification(ancestor);
    }
    
    
    protected Specification<Class<?>> classMethodsAnnotatedWith (final Class<? extends Annotation> annotationClass)
    {
    	return new ClassMethodsAnnotatedWith(annotationClass);
    }
    protected Specification<Class<?>> fieldAnnotatedWith (final Class<? extends Annotation> annotationClass)
    {
    	return new AbstractSpecification<Class<?>> ()
    	{
    		@Override
    		public boolean isSatisfiedBy(Class<?> candidate) {
    			
    			for (Field field : candidate.getDeclaredFields())
    			{
    				if (   field.isAnnotationPresent(annotationClass)   ) 
    				{
    					return true;
    				}
    			}
    			
    			return false;
    		}
    	};
    }

    protected Specification<Class<?>> classAnnotatedWith(final Class<? extends Annotation> klass)
    {
        return new AbstractSpecification<Class<?>>()
        {
            @Override
            public boolean isSatisfiedBy(Class<?> candidate)
            {
                return candidate != null && candidate.getAnnotation(klass) != null;
            }
        };
    }

    // TODO replace this implementation by the one in ClassMethodsAnnotatedWithSpecification
    protected Specification<Class<?>> classImplements(final Class<?> klass)
    {
        return new AbstractSpecification<Class<?>>()
        {
            @Override
            public boolean isSatisfiedBy(Class<?> candidate)
            {
                if (candidate != null && klass.isInterface())
                {
                    for (Class<?> i : candidate.getInterfaces())
                    {
                        if (i.equals(klass))
                        {
                            return true;
                        }
                    }
                }

                return false;
            }
        };
    }

    // * ============================= PLUGIN info and requests * ============================= //

    @Override
    public abstract String name();

    @Override
    public String description()
    {
        return name() + " Nuun Based Plugin.";
    }

    @Override
    public Collection<KernelParamsRequest> kernelParamsRequests()
    {
        return Collections.emptySet();
    }

    public Collection<ClasspathScanRequest> classpathScanRequests()
    {
        return Collections.emptySet();
    }

    @Override
    public Collection<BindingRequest> bindingRequests()
    {
        return Collections.emptySet();
    }

    @Override
    public Collection<Class<? extends Plugin>> requiredPlugins()
    {
        return Collections.emptySet();
    }
    
    @Override
    public Collection<Class<? extends Plugin>> dependentPlugins()
    {
        return Collections.emptySet();
    }

    @Override
    public String pluginPropertiesPrefix()
    {
        return "";
    }

    @Override
    public String pluginPackageRoot()
    {
        return "";
    }

    @Override
    public Object dependencyInjectionDef()
    {
        return null;
    }

    @Override
    public void provideContainerContext(Object containerContext){
    	this.containerContext = containerContext;
    }
    
    @Override
    public Set<URL> computeAdditionalClasspathScan()
    {
        return Collections.emptySet();
    }

    @Override
    public DependencyInjectionProvider dependencyInjectionProvider()
    {
        return null;
    }
    
    
    @Override
    public Map<String, String> kernelParametersAliases()
    {
        return new HashMap<String, String>();
    }

    protected <T> Collection collectionOf(T... items)
    {
        return Arrays.asList(items);
    }

}
