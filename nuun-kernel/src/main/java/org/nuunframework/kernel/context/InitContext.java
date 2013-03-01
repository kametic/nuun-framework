/**
 * 
 */
package org.nuunframework.kernel.context;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.nuunframework.kernel.commons.specification.Specification;
import org.nuunframework.kernel.plugin.Plugin;

import com.google.inject.Module;

/**
 * 
 * @author Epo Jemba
 *
 */
public interface InitContext
{

    public abstract Map<Class<?>, Collection<Class<?>>> scannedSubTypesByParentClass();

    public abstract Map<String, Collection<Class<?>>> scannedSubTypesByParentRegex();

    public abstract Map<Class<? extends Annotation>, Collection<Class<?>>> scannedClassesByAnnotationClass();

    public abstract Map<String, Collection<Class<?>>> scannedClassesByAnnotationRegex();

    public abstract Map<String, Collection<String>> mapPropertiesFilesByPrefix();

    public abstract String getKernelParam(String key);

    public abstract List<Class<?>> classesToBind();

    public abstract List<Module> moduleResults();

    public abstract Collection<String> propertiesFiles();

    public abstract Map<String, Collection<Class<?>>> scannedTypesByRegex();

    public abstract Map<String, Collection<String>> mapResourcesByRegex();

    public abstract Map<Specification, Collection<Class<?>>> scannedTypesBySpecification();    
    
    /**
     * Return instances of the Plugin asked by the plugin
     * 
     * @return the instances of the plugin declared required by the method Plugin.pluginDependenciesRequired()
     */
    public abstract Collection<? extends Plugin> pluginsRequired ();
    

}
