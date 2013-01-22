package org.nuunframework.rest;

import java.util.Collection;

import javax.ws.rs.Path;

import org.nuunframework.kernel.context.RequestContext;
import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.nuunframework.kernel.plugin.Plugin;
import org.nuunframework.kernel.plugin.request.ClasspathScanRequest;
import org.nuunframework.kernel.plugin.request.KernelParamsRequest;
import org.nuunframework.web.NuunWebPlugin;

import com.google.inject.Module;

public class NuunRestPlugin extends AbstractPlugin
{

    public static String NUUN_REST_URL_PATTERN                  = "nuun.rest.url.pattern";
    public static String NUUN_REST_PACKAGE_ROOT                 = "nuun.rest.package.root";
    public static String NUUN_REST_POJO_MAPPING_FEATURE_ENABLED = "nuun.rest.pojo.mapping.feature.enabled";

    private String       packageRoot;

    private boolean      enablePojoMappingFeature              = true;

    private String       urlPattern;

    private Module       module;

    @Override
    public String name()
    {
        return "nuun-rest-plugin";
    }

    @Override
    public Object dependencyInjectionDef()
    {

        if (module == null)
        {

            module = new NuunRestModule(urlPattern, enablePojoMappingFeature);
        }

        return module;
    }

    @Override
    public void init(RequestContext requestContext)
    {

        this.urlPattern = requestContext.getKernelParam(NUUN_REST_URL_PATTERN);
        String tmp = requestContext.getKernelParam(NUUN_REST_POJO_MAPPING_FEATURE_ENABLED);
        if (tmp != null && !tmp.isEmpty())
        {
            this.enablePojoMappingFeature = Boolean.valueOf(tmp);
        }
    }

    
    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests()
    {
        return classpathScanRequestBuilder().annotationType(Path.class).build();
    }
    

    /*
     * (non-Javadoc)
     * @see com.inetpsa.nuun.core.plugin.AbstractStsPlugin#kernelParamsRequired()
     */

    @Override
    public Collection<KernelParamsRequest> kernelParamsRequests()
    {
        return kernelParamsRequestBuilder()
                .mandatory(NUUN_REST_URL_PATTERN)
                .optional(NUUN_REST_POJO_MAPPING_FEATURE_ENABLED)
                .build();
    }


    /*
     * (non-Javadoc)
     * @see com.inetpsa.nuun.core.plugin.AbstractStsPlugin#pluginsRequired()
     */
    @SuppressWarnings({
            "unchecked", "rawtypes"
    })
    @Override
    public Collection<Class<? extends Plugin>> pluginDependenciesRequired()
    {
        return (Collection) collectionOf(NuunWebPlugin.class);
    }

}
