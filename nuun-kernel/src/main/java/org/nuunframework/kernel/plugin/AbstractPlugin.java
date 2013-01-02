/**
 * 
 */
package org.nuunframework.kernel.plugin;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.nuunframework.kernel.context.Context;
import org.nuunframework.kernel.context.RequestContext;
import org.nuunframework.kernel.plugin.request.BindingRequest;
import org.nuunframework.kernel.plugin.request.BindingRequestBuilder;
import org.nuunframework.kernel.plugin.request.ClasspathScanRequest;
import org.nuunframework.kernel.plugin.request.ClasspathScanRequestBuilder;
import org.nuunframework.kernel.plugin.request.KernelParamsRequest;
import org.nuunframework.kernel.plugin.request.KernelParamsRequestBuilder;


/**
 * @author Epo Jemba
 * 
 */
public abstract class AbstractPlugin implements Plugin
{

    protected Context                         context = null;
    @SuppressWarnings("unused")
    private Map<String, String>               kernelParams;
    @SuppressWarnings("unused")
    private RequestContext                     requestContext;
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
     * ============================= PLUGIN LIFE CYCLE USED BY KERNEL
     * =============================
     **/

    @Override
    public void init(RequestContext requestContext)
    {
        this.requestContext = requestContext;
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

//    /**
//     * ============================= PLUGIN Utilities Helpers =============================
//     * 
//     * 
//     **/

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

    protected BindingRequestBuilder bindingRequestsBuilder()
    {
        bindingBuilder.reset();
        return bindingBuilder;
    }

    /**
     * ============================= PLUGIN info and requests
     * =============================
     **/

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
    public Collection<Class<? extends Plugin>> pluginDependenciesRequired()
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

    protected <T> Collection<T> collectionOf(T... items)
    {
        return Arrays.asList(items);
    }

}
