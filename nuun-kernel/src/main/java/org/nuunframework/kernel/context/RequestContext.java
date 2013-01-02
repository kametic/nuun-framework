/**
 * 
 */
package org.nuunframework.kernel.context;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.inject.Module;

/**
 * 
 * @author Epo Jemba
 *
 */
public interface RequestContext
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

//    public abstract Map<Class<?>, Collection<Class<?>>> scannedTypesByClass();

}
