package org.nuunframework.rest;

import java.util.Collection;

import javax.inject.Scope;
import javax.ws.rs.Path;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.nuunframework.kernel.commons.specification.Specification;
import org.nuunframework.kernel.context.InitContext;
import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.nuunframework.kernel.plugin.Plugin;
import org.nuunframework.kernel.plugin.request.BindingRequest;
import org.nuunframework.kernel.plugin.request.KernelParamsRequest;
import org.nuunframework.web.NuunWebPlugin;

import com.google.inject.Module;
import com.google.inject.Scopes;

public class NuunRestPlugin extends AbstractPlugin
{

    public static String NUUN_REST_URL_PATTERN                  = "nuun.rest.url.pattern";
    public static String NUUN_REST_PACKAGE_ROOT                 = "nuun.rest.package.root";
    public static String NUUN_REST_POJO_MAPPING_FEATURE_ENABLED = "nuun.rest.pojo.mapping.feature.enabled";

    private boolean      enablePojoMappingFeature               = true;

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
    public void init(InitContext initContext)
    {

        this.urlPattern = initContext.getKernelParam(NUUN_REST_URL_PATTERN);
        String tmp = initContext.getKernelParam(NUUN_REST_POJO_MAPPING_FEATURE_ENABLED);
        if (tmp != null && !tmp.isEmpty())
        {
            this.enablePojoMappingFeature = Boolean.valueOf(tmp);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<BindingRequest> bindingRequests()
    {
        
        Specification<Class<?>> specificationForNoScope = or ( 
                classIsAnnotatedWith(Path.class) ,
                classMethodsAnnotatedWith(Path.class) 
   		);
        Specification<Class<?>> specificationForSingletonScop =  or(                             
                and( classIsAnnotatedWith(Provider.class) , classIsImplementing(MessageBodyWriter.class)) , 
                and( classIsAnnotatedWith(Provider.class) , classIsImplementing(ContextResolver.class)) ,
                and( classIsAnnotatedWith(Provider.class) , classIsImplementing(MessageBodyReader.class)) ,
                and( classIsAnnotatedWith(Provider.class) , classIsImplementing(ExceptionMapper.class))
               ) ;
                
        return bindingRequestsBuilder()
                .specification(specificationForNoScope)
                .specification(specificationForSingletonScop,Scopes.SINGLETON)
                .build();
        
//        return bindingRequestsBuilder() // 
//                .annotationType(Path.class) // 
//                .annotationType(Provider.class) // 
//                .build(); // 
    }

    /*
     * (non-Javadoc)
     * @see com.inetpsa.nuun.core.plugin.AbstractStsPlugin#kernelParamsRequired()
     */

    @Override
    public Collection<KernelParamsRequest> kernelParamsRequests()
    {
        return kernelParamsRequestBuilder().mandatory(NUUN_REST_URL_PATTERN).optional(NUUN_REST_POJO_MAPPING_FEATURE_ENABLED).build();
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
