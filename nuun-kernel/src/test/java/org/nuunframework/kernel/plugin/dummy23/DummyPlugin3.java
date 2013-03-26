/**
 * 
 */
package org.nuunframework.kernel.plugin.dummy23;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Collection;
import java.util.Map;

import org.nuunframework.kernel.context.InitContext;
import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.nuunframework.kernel.plugin.Plugin;
import org.nuunframework.kernel.plugin.request.ClasspathScanRequest;
import org.nuunframework.kernel.plugins.configuration.NuunConfigurationPlugin;

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
    public void init(InitContext initContext)
    {
        Map<String, Collection<String>> mapResourcesByRegex = initContext.mapResourcesByRegex();
        
        assertThat( mapResourcesByRegex.get(resourcesRegex1) ).isNotNull();
        assertThat( mapResourcesByRegex.get(resourcesRegex1) ).hasSize(1);
        assertThat( mapResourcesByRegex.get(resourcesRegex1) ).contains("resource-to-reach.json");
        
        assertThat( mapResourcesByRegex.get(resourcesRegex2) ).isNotNull();
        assertThat( mapResourcesByRegex.get(resourcesRegex2) ).hasSize(2);
        assertThat( mapResourcesByRegex.get(resourcesRegex2) ).contains("internal/sample1-applicationContext-business.xml");
        assertThat( mapResourcesByRegex.get(resourcesRegex2) ).contains("internal/sample2-applicationContext-persistence.xml");
        
        NuunConfigurationPlugin confPlugin = (NuunConfigurationPlugin) initContext.pluginsRequired().iterator().next();
        
        assertThat(confPlugin.getConfiguration().getString("value1")).isEqualTo("lorem ipsum");
        
        
    }

     /* (non-Javadoc)
     * @see org.nuunframework.kernel.plugin.AbstractPlugin#pluginsRequired()
     */
     @SuppressWarnings({
            "unchecked", "rawtypes"
    })
    @Override
     public Collection<Class<? extends Plugin>> requiredPlugins()
     {
         return (Collection) collectionOf(NuunConfigurationPlugin.class);
     }

}
